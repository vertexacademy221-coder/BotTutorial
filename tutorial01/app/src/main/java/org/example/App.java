/*
 * Ce premier tuoriel vous expliquera tout ce que vous devez 
 * savoir pour créer votre premier bot Telegram.
 * 
 * En résumé, l' API Telegram Bot peut être considérée comme un logiciel 
 * qui fournit des réponses encodées en JSON à vos requêtes.
 * Un bot, en revanche, est essentiellement une routine, un logiciel ou 
 * un script qui interroge l'API via une requête HTTPS et attend une réponse. 
 * Il existe plusieurs types de requêtes possibles, ainsi que de nombreux objets 
 * différents pouvant être utilisés et reçus en réponse.
 * 
 */

package org.example;

public class App {
    public String getGreeting() {
        return "Hello World!";
    }

    public static void main(String[] args) {
        System.out.println(new App().getGreeting());
    }
}
