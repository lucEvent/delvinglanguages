package com.delvinglanguages.kernel.game;

import com.delvinglanguages.kernel.set.DReferences;

import java.util.ArrayList;

public class CompleteGame extends Game {

    public class Action {

        public int position;
        public char letter;
        public String string;
        public Action replaceBy;
        public int visibleUntil;

        public Action(int pos, char let, String act) {
            position = pos;
            letter = let;
            string = act;
            visibleUntil = pos;// Al menos tiene que ser visible hasta su
            // posicion
        }
    }

    public CompleteGame(DReferences references) {
        super(references);
    }

    public Action[] char_merger(String word, int size) {
        StringBuilder temp = new StringBuilder(word.toUpperCase());
        boolean dictionary[] = new boolean['z' - 'a' + 1];
        for (int i = 0; i < dictionary.length; i++) {
            dictionary[i] = false;
        }
        // Aplicando un filtros y moldeando
        ArrayList<Action> validChars = new ArrayList<Action>();
        int position = 0;
        while (temp.length() > 0) {
            char c = temp.charAt(0);
            if (!Character.isLetter(c)) {
                Action act = validChars.get(position - 1);
                char fin;
                if (c == '(') {
                    fin = ')';
                } else if (c == '[') {
                    fin = ']';
                } else if (c == '{') {
                    fin = '}';
                } else {
                    act.string = act.string + c;
                    temp.deleteCharAt(0);
                    continue;
                }
                int ifin = temp.indexOf("" + fin);
                if (ifin != -1) {
                    act.string = act.string + temp.substring(0, ifin);
                    temp.delete(0, ifin);
                } else {
                    act.string = act.string + c;
                    temp.deleteCharAt(0);
                }
            } else {
                try {
                    validChars.add(new Action(position, c, "" + c));
                    position++;
                    temp.deleteCharAt(0);
                    if (!dictionary[c - 'A']) {
                        dictionary[c - 'A'] = true;
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.out.println("AIOOBException por: " + c + " ("
                            + (int) c + ")");
                }
            }
        }

        // Comprovamos que las (size o validChars.size) primeras Acciones no
        // estan repetidas
        int minsize = validChars.size() < size ? validChars.size() : size;
        for (int i = 1; i < minsize; i++) {
            Action act1 = validChars.get(i);
            for (int j = 0; j < i; j++) {
                Action act2 = validChars.get(j);
                if (act1.letter == act2.letter) {
                    getyoungeraction(act2).replaceBy = act1;
                    validChars.remove(i);
                    i--;
                    minsize = validChars.size() < size ? validChars.size()
                            : size;
                    break;
                }
            }
        }

        // Creamos lo que sera el teclado a devolver
        Action teclado[] = new Action[size];
        // Metemos las (size o validChars.size) primeras Acciones
        minsize = validChars.size() < size ? validChars.size() : size;
        for (int i = 0; i < minsize; i++) {
            int pos;
            do { // Elejimos una posicion libre al azar
                pos = nextInt(size);
            } while (teclado[pos] != null);
            Action act = validChars.remove(0);
            teclado[pos] = act;
        }
        // Si todavia quedan letras por meter:
        position = minsize + 1;
        while (!validChars.isEmpty()) {
            Action next = validChars.remove(0);
            // Miramos si la letra actual esta (necesitamos recursividad)
            boolean isit = false;
            for (int j = 0; j < teclado.length; j++) {
                Action old = ischarinto(teclado[j], next);
                if (old != null) {
                    old.replaceBy = next;
                    isit = true;
                    break;
                }
            }
            // Si no esta buscamos la letra mas joven para reemplazar
            if (!isit) {
                Action old = getyoungeraction(teclado[0]);
                for (int j = 1; j < teclado.length; j++) {
                    Action tmpA = getyoungeraction(teclado[j]);
                    if (tmpA.visibleUntil < old.visibleUntil) {
                        old = tmpA;
                    }
                }
                // y reemplazamos
                old.replaceBy = next;
            }
        }
        // Miramos si alguna posicion del teclado ha quedado vacia
        for (int i = 0; i < teclado.length; i++) {
            if (teclado[i] == null) {
                char c = nextLetter(true);
                for (int j = 0; j < teclado.length; j++) {
                    if (teclado[j] != null && teclado[j].letter == c) {
                        c = nextLetter(true);
                        j = -1;
                    }
                }
                teclado[i] = new Action(-1, c, "");
            }
        }
        return teclado;
    }

    private Action getyoungeraction(Action action) {
        if (action.replaceBy != null) {
            return getyoungeraction(action.replaceBy);
        }
        return action;
    }

    private Action ischarinto(Action host, Action guest) {
        if (host.replaceBy != null) {
            return ischarinto(host.replaceBy, guest);
        }
        if (host.letter == guest.letter) {
            return host;
        }
        return null;
    }

}
