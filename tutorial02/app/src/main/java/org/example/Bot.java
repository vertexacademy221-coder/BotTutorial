package org.example;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import org.telegram.telegrambots.meta.api.objects.Contact;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendContact;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;


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
			"\nFrom : " + u_firstName + (u_lastName == null || u_lastName.isEmpty() ? "" : " " + u_lastName) +
			"\nID : " + m_id +
			"\nIs a bot ? " + isBot + 
			"\nLanguage code : " + languageCode);

		// Echo : Send back the user message
		sendMessage(m_id, msg.getText());
		sendPic(m_id);

	}

	public long getId()
	{
		return m_id;
	}

	public void sendMessage (Long who, String what)
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

	public void sendContact (long who)
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

	public void sendPic(Long who)
	{
		SendPhoto pic = new SendPhoto().builder()
					.chatId(who.toString())
					.photo(new InputFile ("https://png.pngtree.com/background/20230519/original/pngtree-this-is-a-picture-of-a-tiger-cub-that-looks-straight-picture-image_2660243.jpg"))
					.caption("This is a little cat")
					.build();
		try
		{
			execute (pic);
		}
		catch (TelegramApiException e)
		{
			e.printStackTrace();
		}
	}
}