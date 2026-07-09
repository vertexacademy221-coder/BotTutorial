package org.example;

import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendContact;
import org.telegram.telegrambots.meta.api.objects.Contact;


public class Bot extends TelegramLongPollingBot
{
	private static long m_id;

	// Get and store your telegram bot token here
	private static final String BOT_TOKEN = System.getenv("BOT_TOKEN");

	@Override
	public String getBotUsername() {
	  return "Gainde221";
	}

	@Override
	public String getBotToken() {
	  return BOT_TOKEN;
	}

	@Override
	public void onUpdateReceived(Update update) {
		var msg = update.getMessage();
		var from = msg.getFrom();
		var u_firstName = from.getFirstName();
		var u_lastName  = from.getLastName();
		var isBot = from.getIsBot();
		var languageCode = from.getLanguageCode();
		m_id = from.getId();
		
		System.out.println("Message : " + msg.getText()+ 
			"\nFrom : " + u_firstName + " " + u_lastName +
			"\nIs a bot ? " + isBot + 
			"\nLanguage code : " + languageCode);
		//System.out.println (update);
	}

	public long getId()
	{
		return m_id;
	}

	public void sendMessage (long who, String what) throws TelegramApiException
	{
		String sender = Long.toString(who);
		SendMessage sm = SendMessage.builder()
						.chatId(sender)
						.text(what)
						.build();
		try
		{
			execute (sm);
		}
		catch (TelegramApiException e)
		{
			throw new RuntimeException (e);
		}
	}

	public void sendContact (long who) throws TelegramApiException
	{
		Contact user = new Contact();
		user.setPhoneNumber("+221775778011");
		user.setFirstName("NDIAYE");
		user.setLastName("Yossep");

		String sender = Long.toString(who);

		SendContact cm = new SendContact().builder()
							.chatId(sender)
							.firstName(user.getFirstName())
							.lastName(user.getLastName())
							.phoneNumber(user.getPhoneNumber())
							.build();
		try 
		{
			execute (cm);
		}
		catch (TelegramApiException e)
		{
			throw new RuntimeException (e);
		}
	}
}