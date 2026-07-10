package org.example;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import org.telegram.telegrambots.meta.api.objects.Contact;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;

import org.telegram.telegrambots.meta.api.methods.CopyMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendContact;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;


public class Bot extends TelegramLongPollingBot
{
	// Unique user ID
	private long m_id;
	private final String School_name = "EPF AFRICA";
	private final String School_desc = "En 2022, la Fondation EPF prend la décision de créer une école en Afrique, " +
	"EPF Africa, à Dakar, pour répondre à sa mission originelle de former des ingénieurs durables." +
	" L’ouverture de l’EPF AFRICA s’inscrit dans la stratégie de développement de la Fondation qui entame une nouvelle page " +
	"de son histoire avec son installation hors de France.\n\n" +
	"L'EPF Africa est reconnue par le ministère de l’Enseignement Supérieur, de la Recherche et de l'Innovation et habilitée " +
	"par l'Autorité Nationale d'Assurance Qualité de l'Enseignement Supérieur (ANAQ-SUP) du Sénégal.\n\n" +
	"Elle a pour vocation de former des cadres techniques de haut niveau et des ingénieurs généralistes innovants," +
	"responsables et de dimension internationale, capables de répondre aux défis technologiques et aux besoins de développement du continent africain.\n\n" +
	"La mission de l’EPF Africa s’articule autour de 3 valeurs fortes 'Innovation, Audace et Engagement' qui contribuent à forger" +
	" l’identité de l’Ingénieur EPF acteur du changement et humaniste engagé";

	// Store the bot current mode
	private boolean screaming = false;

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

		if (msg.isCommand())
		{
			if (msg.getText().equals("/scream")) 			// If the command was /scream, we switch gears
				screaming = true;
			else if (msg.getText().equals("/whisper"))		// Otherwise we return to normal
				screaming = false;
			else if (msg.getText().equals("/start"))
				start(m_id);
			else if (msg.getText().equals("/help"))
				printHelp(m_id);
			return;							// We don't want to echo commands
		}

		if (screaming)
		{
			scream(m_id, msg);
		}
		else
		{
			copyMessage(m_id, msg.getMessageId());
		}
	}

	public long getId()
	{
		return m_id;
	}

	public void copyMessage (Long who, Integer msgId)
	{
		CopyMessage cm = CopyMessage.builder()
						.fromChatId(who.toString())   // We copy from the user
					.chatId(who.toString())			// and send back to him
				.messageId(msgId)					// Specify what message
			.build();
		try
		{
			execute (cm);
		}
		catch (TelegramApiException e)
		{
			e.printStackTrace();
		}
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
		user.setFirstName("BINYOUM");
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

	/* =====  PRIVATE ===== */

	private void scream (Long who, Message message)
	{
		if (message.hasText()) 
		{
			sendMessage (who, message.getText().toUpperCase());
		}
		else 
		{
			copyMessage(who, message.getMessageId());
		}
	}

	private void start(Long who)
	{
		SendMessage msg = new SendMessage().builder()
							.chatId(who.toString())
						.text("<b>" + School_name  + "</b>\n\n" + School_desc)
					.build();

		msg.setParseMode("HTML");

		try 
		{
			execute (msg);
		}
		catch (TelegramApiException e)
		{
			e.printStackTrace();
		}

	}

	private void printHelp (Long who)
	{
		
	}
}