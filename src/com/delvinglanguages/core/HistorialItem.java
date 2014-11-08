package com.delvinglanguages.core;

public class HistorialItem {
    
	private static final char SEP = ',';
	
    public final int type;
 
    public final String[] descriptions;
 
    public final long time;
 
    public final int[] ids;
    
    
    public HistorialItem(int type, String[] descriptions, int[] itemIDs, long time) {
        this.type = type;
        this.descriptions = descriptions;
        this.time = time;
        this.ids = itemIDs;
    }

    public HistorialItem(String params) {
    	String[] info = params.split("" +SEP);
        type = Integer.parseInt(info[0]);
        int ndesc = Integer.parseInt(info[1]);
        descriptions = new String[ndesc];
        for (int i = 0; i < ndesc; i++) {
			descriptions[i] = info[i+2];
		}
        time = Long.parseLong(info[ndesc+2]);
        int nids = Integer.parseInt(info[ndesc+3]);
        ids = new int[nids];
        for (int i = 0; i < nids; i++) {
			ids[i] = Integer.parseInt(info[i+4]);
		}
    }
    
    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();
        res.append(type).append(SEP);
        res.append(descriptions.length).append(SEP);
        for (String desc : descriptions) {
            res.append(desc).append(SEP);
		}
        res.append(time);
        res.append(ids.length);
        for (int id : ids) {
            res.append(SEP).append(id);
		}
        return res.toString();
    }
 
}
