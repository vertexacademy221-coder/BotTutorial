/*
 * This first tutorial will explain everything you need to 
 * know to create your first Telegram bot.
 * 
 * In short, the Telegram Bot API can be thought of as software 
 * that provides JSON-encoded responses to your requests.
 * A bot, on the other hand, is essentially a routine, a program, or 
 * a script that queries the API via an HTTPS request and waits for a response. 
 * There are several possible types of requests, as well as many 
 * different objects that can be used and received in response.
 * 
 */
package org.example;

import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import org.telegram.telegrambots.meta.TelegramBotsApi;

public class App {

    private static Bot bot = new Bot();

    public String getGreeting() {
        return "Hello World!";
    }

    public static void main(String[] args) throws TelegramApiException{

        TelegramBotsApi botApi = new TelegramBotsApi(DefaultBotSession.class);
        botApi.registerBot(bot); 

        long botID = bot.getId();
        bot.sendMessage(8913673783L, "hello World");
        bot.sendContact(8913673783L);
    }
}
