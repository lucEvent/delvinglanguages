package com.delvinglanguages.server;

import com.delvinglanguages.server.db.ItemHistoryRecord;
import com.delvinglanguages.server.db.Language;
import com.delvinglanguages.server.db.LanguageHistoryRecord;
import com.delvinglanguages.server.db.LanguageItem;
import com.delvinglanguages.server.util.UpdateWrapper;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.config.Named;
import com.google.api.server.spi.response.NotFoundException;
import com.google.appengine.api.oauth.OAuthRequestException;
import com.google.appengine.api.users.User;
import com.googlecode.objectify.ObjectifyService;

import java.util.List;
import java.util.TreeSet;

import javax.annotation.Nonnull;

import static com.googlecode.objectify.ObjectifyService.ofy;

@Api(
        name = "delvingApi",
        version = "v1",
        namespace = @ApiNamespace(
                ownerDomain = "server.delvinglanguages.com",
                ownerName = "server.delvinglanguages.com",
                packagePath = ""
        ),
        scopes = {Ids.EMAIL_SCOPE},
        clientIds = {Ids.WEB_CLIENT_ID, Ids.ANDROID_CLIENT_ID, Ids.ANDROID_DEBUG_ID, com.google.api.server.spi.Constant.API_EXPLORER_CLIENT_ID},
        audiences = {Ids.ANDROID_AUDIENCE}
)
public class DelvingApiService {

    static {
        ObjectifyService.register(Language.class);
        ObjectifyService.register(LanguageItem.class);
        ObjectifyService.register(LanguageHistoryRecord.class);
        ObjectifyService.register(ItemHistoryRecord.class);
    }

    public static final int EVENT_CREATED = 1;
    public static final int EVENT_UPDATED = 2;
    public static final int EVENT_DELETED = 3;

    @ApiMethod(name = "updateLanguage", path = "update_language")
    public void updateLanguage(User user, Language language)
            throws NotFoundException, OAuthRequestException
    {
        if (user == null) throw new OAuthRequestException("Invalid user.");

        Language dbl = getLanguage(user, language.id);
        if (dbl == null) {
            // add language
            language.user_id = user.getEmail();

            ofy().save().entity(language).now();

            recordHR(user, language, EVENT_CREATED);

        } else {
            // update language
            dbl.code = language.code;
            dbl.settings = language.settings;
            dbl.name = language.name;
            dbl.isPublic = language.isPublic;

            ofy().save().entity(dbl).now();

            recordHR(user, dbl, EVENT_UPDATED);
        }
    }

    @ApiMethod(name = "removeLanguage", path = "remove_language")
    public void removeLanguage(User user, @Named("id") int id)
            throws OAuthRequestException
    {
        if (user == null) throw new OAuthRequestException("Invalid user.");

        Language language = getLanguage(user, id);
        if (language == null)
            return;

        ofy().delete().entity(language);
        ofy().delete().entities(
                ofy().load().type(LanguageItem.class)
                        .filter("language_id", id)
                        .filter("user_id", user.getEmail())
                        .list()
        );

        recordHR(user, language, EVENT_DELETED);
    }

    private Language getLanguage(User user, int language_id)
    {
        return ofy().load().type(Language.class)
                .filter("user_id", user.getEmail())
                .filter("id", language_id)
                .first().now();
    }

    /**
     * ************** Language Item ****************
     **/
    @ApiMethod(name = "updateLanguageItem", path = "updateLanguageItem")
    public void updateLanguageItem(User user, LanguageItem item)
            throws NotFoundException, OAuthRequestException
    {
        if (user == null) throw new OAuthRequestException("Invalid user.");

        Language language = getLanguage(user, item.language_id);
        if (language == null)
            throw new NotFoundException("language does not exist [id: " + item.language_id + "]");

        LanguageItem dbli = getLanguageItem(user, item.language_id, item.id, item.type);
        if (dbli == null) {
            //Add item
            item.user_id = user.getEmail();

            switch (item.type) {
                case LanguageItem.TYPE_REFERENCE:
                    language.nWords++;
                    break;
                case LanguageItem.TYPE_DRAWER_REFERENCE:
                    language.nDrawerWords++;
                    break;
                case LanguageItem.TYPE_THEME:
                    language.nThemes++;
                    break;
                case LanguageItem.TYPE_TEST:
                    language.nTests++;
                    break;
            }
            ofy().save().entity(language).now();
            ofy().save().entity(item).now();

            recordIHR(user, item.language_id, item.id, item.type, ItemHistoryRecord.EVENT_CREATED);

        } else {
            //Update item
            dbli.wrapper = item.wrapper;

            ofy().save().entity(dbli).now();

            recordIHR(user, dbli.language_id, dbli.id, dbli.type, ItemHistoryRecord.EVENT_UPDATED);
        }
    }

    @ApiMethod(name = "removeLanguageItem", path = "remove_language_item")
    public void removeLanguageItem(User user, @Named("lang_id") int lang_id, @Named("item_id") int item_id, @Named("type") int type)
            throws OAuthRequestException
    {
        if (user == null) throw new OAuthRequestException("Invalid user.");

        Language language = getLanguage(user, lang_id);
        if (language == null)
            return;

        LanguageItem item = getLanguageItem(user, lang_id, item_id, type);
        if (item == null)
            return;

        switch (item.type) {
            case LanguageItem.TYPE_REFERENCE:
                language.nWords--;
                break;
            case LanguageItem.TYPE_DRAWER_REFERENCE:
                language.nDrawerWords--;
                break;
            case LanguageItem.TYPE_THEME:
                language.nThemes--;
                break;
            case LanguageItem.TYPE_TEST:
                language.nTests--;
                break;
        }
        ofy().save().entity(language).now();
        ofy().delete().entity(item);

        recordIHR(user, item.language_id, item.id, item.type, ItemHistoryRecord.EVENT_DELETED);
    }

    private LanguageItem getLanguageItem(User user, int language_id, int item_id, int type)
    {
        return ofy().load().type(LanguageItem.class)
                .filter("id", item_id)
                .filter("language_id", language_id)
                .filter("type", type)
                .filter("user_id", user.getEmail())
                .first().now();
    }

    /**
     * ************** Others ****************
     **/
    @ApiMethod(name = "getServerUpdate", path = "getServerUpdate")
    public UpdateWrapper getServerUpdate(User user, @Named("last_sync") long lastSync)
            throws OAuthRequestException
    {
        if (user == null) throw new OAuthRequestException("Invalid user.");

        UpdateWrapper res = new UpdateWrapper();
        if (lastSync == 0) {
            List<Language> languages = getLanguagesOf(user);

            if (languages != null && !languages.isEmpty()) {
                res.languages_to_add.addAll(languages);
                for (int i = 0; i < res.languages_to_add.size(); i++) {
                    Language l = res.languages_to_add.get(i);
                    res.items_to_add.addAll(getLanguageItemsOf(user, l.id));
                }
            }

        } else {
            TreeSet<LanguageHistoryRecord> lang_records = new TreeSet<>();
            lang_records.addAll(
                    ofy().load().type(LanguageHistoryRecord.class)
                            .filter("user_id", user.getEmail())
                            .list()
            );

            for (LanguageHistoryRecord r : lang_records) {
                if (r.time <= lastSync)
                    continue;

                Language language;
                switch (r.event) {
                    case EVENT_CREATED:
                        language = getLanguage(user, r.language_id);
                        res.languages_to_add.add(language);
                        break;

                    case EVENT_UPDATED:
                        language = getLanguage(user, r.language_id);
                        if (!res.languages_to_add.contains(language))
                            res.languages_to_update.add(language);
                        break;

                    case EVENT_DELETED:
                        res.languages_to_remove.add(r.language_id);
                        break;
                }
            }

            TreeSet<ItemHistoryRecord> item_records = new TreeSet<>();
            item_records.addAll(
                    ofy().load().type(ItemHistoryRecord.class)
                            .filter("user_id", user.getEmail())
                            .list()
            );

            for (ItemHistoryRecord ir : item_records) {
                if (ir.time <= lastSync)
                    continue;

                LanguageItem item;
                switch (ir.event) {
                    case ItemHistoryRecord.EVENT_CREATED:
                        item = getLanguageItem(user, ir.language_id, ir.id, ir.type);
                        res.items_to_add.add(item);
                        break;

                    case ItemHistoryRecord.EVENT_UPDATED:
                        item = getLanguageItem(user, ir.language_id, ir.id, ir.type);
                        if (!res.items_to_add.contains(item))
                            res.items_to_update.add(item);
                        break;

                    case ItemHistoryRecord.EVENT_DELETED:
                        item = new LanguageItem();
                        item.language_id = ir.language_id;
                        item.id = ir.id;
                        item.type = ir.type;
                        res.items_to_remove.add(item);
                        break;
                }
            }
        }

        return res;
    }

    private List<Language> getLanguagesOf(User user) throws OAuthRequestException
    {
        return ofy().load().type(Language.class)
                .filter("user_id", user.getEmail())
                .list();
    }

    private List<LanguageItem> getLanguageItemsOf(User user, int language_id) throws OAuthRequestException
    {
        return ofy().load().type(LanguageItem.class)
                .filter("user_id", user.getEmail())
                .filter("language_id", language_id)
                .list();
    }

    @ApiMethod(name = "getPublicLanguages", path = "public_languages")
    public List<Language> getPublicLanguages(User user) throws OAuthRequestException
    {
        if (user == null) throw new OAuthRequestException("Invalid user.");

        return ofy().load().type(Language.class).filter("isPublic", true).list();
    }

    private void recordHR(@Nonnull User user, @Nonnull Language language, int event)
    {
    /*    LanguageHistoryRecord hr = new LanguageHistoryRecord();
        hr.user_id = user.getEmail();
        hr.language_id = language.id;
        hr.event = event;
        hr.time = System.currentTimeMillis();

        ofy().save().entity(hr).now();*/
    }

    private void recordIHR(@Nonnull User user, int language_id, int item_id, int type, int event)
    {
    /*    ItemHistoryRecord hr = new ItemHistoryRecord();
        hr.user_id = user.getEmail();
        hr.language_id = language_id;
        hr.event = event;
        hr.time = System.currentTimeMillis();
        hr.id = item_id;
        hr.type = type;

        ofy().save().entity(hr).now();*/
    }

}
