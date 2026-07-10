package org.example;

import java.util.List;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import org.telegram.telegrambots.meta.api.objects.Contact;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.methods.CopyMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendContact;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;

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
	" l’identité de l’Ingénieur EPF acteur du changement et humaniste engagé\n\n";

	// Store the bot current mode
	private boolean screaming = false;

	// Keyboard markup buttons
	private InlineKeyboardMarkup keyboardM1;
	private InlineKeyboardMarkup keyboardM2;

	// Get and store your telegram bot token here
	private static final String BOT_TOKEN = System.getenv("BOT_TOKEN");

	/*[getBotUsername]*/
	@Override
	public String getBotUsername() {
	  return "Gainde221";
	}

	/*[getBotToken]*/
	@Override
	public String getBotToken() {
	  return BOT_TOKEN;
	}

	/*[onUpdateReceived]*/
	@Override
	public void onUpdateReceived(Update update) {

	    // --- 1. PRÉPARATION DES CLAVIERS (Toujours disponibles) ---
	    var next = InlineKeyboardButton.builder().text("Next").callbackData("next").build();
	    var back = InlineKeyboardButton.builder().text("Back").callbackData("back").build();
	    var url = InlineKeyboardButton.builder().text("Tutorial").url("https://core.telegram.org/bots/api").build();

	    keyboardM1 = InlineKeyboardMarkup.builder().keyboardRow(List.of(next)).build();  
	    keyboardM2 = InlineKeyboardMarkup.builder().keyboardRow(List.of(back)).keyboardRow(List.of(url)).build();
	    
	    // --- 2. TRAITEMENT SI C'EST UN CLIC SUR UN BOUTON (CallbackQuery) ---
	    if (update.hasCallbackQuery()) {
	        String queryId = update.getCallbackQuery().getId();
	        String data    = update.getCallbackQuery().getData();
	        Long who       = update.getCallbackQuery().getMessage().getChatId();
	        int msgId      = update.getCallbackQuery().getMessage().getMessageId();
	        
	        // On envoie les infos à ta méthode dédiée
	        buttonTap(who, queryId, data, msgId);
	        return; // Important : On arrête la méthode ici pour ce clic
	    }

	    // --- 3. TRAITEMENT SI C'EST UN MESSAGE TEXTE CLASSIQUE ---
	    if (update.hasMessage()) {
	        var msg = update.getMessage();
	        var from = msg.getFrom();
	        m_id = from.getId(); // Ton ID utilisateur pour les commandes

	        // Si c'est une commande (/menu, /start, etc.)
	        if (msg.isCommand()) {
	            if (msg.getText().equals("/scream")) {
	                screaming = true;
	            } else if (msg.getText().equals("/whisper")) {
	                screaming = false;
	            } else if (msg.getText().equals("/start")) {
	                start(m_id);
	            } else if (msg.getText().equals("/menu")) {
	                sendMenu(m_id, "<b>Menu 1</b>", keyboardM1); // Envoie le Menu 1 avec le bouton "Next"
             	} else if (msg.getText().equals("/back")) {
             		sendMenu(m_id, "<b>Menu 2</b>", keyboardM2);	// Envoid le Menu 2 avec le bouton "Back"
	            } else if (msg.getText().equals("/help")) {
	                printHelp(m_id);
	            }
	            return; // On ne veut pas "echo" les commandes
	        }

	        // Si ce n'est pas une commande, on traite le texte normal
	        if (screaming) {
	            scream(m_id, msg);
	        } else {
	            copyMessage(m_id, msg.getMessageId());
	        }
	    }
	}
	/*[getId]*/
	public long getId()
	{
		return m_id;
	}

	/*[copyMessage]*/
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

	/*[sendMessage]*/
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

	/*[sendContact]*/
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

	/*[sendPic]*/
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
	/*[start]*/
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

	/*[start]*/
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

	/*[printHelp]*/
	private void printHelp (Long who)
	{
		SendMessage sm = new SendMessage().builder()
										.chatId(who.toString())
									.text("I Will be back very soon...")
								.build();
		try
		{
			execute (sm);
		}
		catch (TelegramApiException e) {
			e.printStackTrace();
		}
	}

	/*[sendMenu]*/
	private void sendMenu(Long who, String txt, InlineKeyboardMarkup kb)
	{
		SendMessage sm = new SendMessage().builder()
								.chatId(who.toString())
							.parseMode("HTML")
						.text(txt)
					.replyMarkup(kb)
				.build();
		try 
		{
			execute (sm);
		}
		catch (TelegramApiException e)
		{
			e.printStackTrace();
		}
	}

	/*[buttonTap]*/
	private void buttonTap (Long who, String queryId, String data, int msgId)
	{
		// Build a new message based on an @msgId (existing one) sent by @who
		EditMessageText newTxt = new EditMessageText().builder()
									.chatId(who.toString())
								.messageId(msgId)
							.text("")
						.build();

		EditMessageReplyMarkup editMarkup = new EditMessageReplyMarkup().builder()
											.chatId(who.toString())	// ID du sender
										.messageId(msgId)		// ID du message à modifier
									.build();	
		if (data.equals("next"))
		{
			newTxt.setText("MENU 2");
			editMarkup.setReplyMarkup(keyboardM2);
		}
		else if (data.equals("back"))
		{
			newTxt.setText("MENU 1");
			editMarkup.setReplyMarkup(keyboardM1);
		}

		AnswerCallbackQuery close = new AnswerCallbackQuery().builder()
								.callbackQueryId(queryId)
							.build();
		try {
			execute(close);
			execute(newTxt);
			execute(editMarkup);	
		}
		catch (TelegramApiException e){
			e.printStackTrace();
		}
		
	}
}