package ba.bitcamp.view;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ba.bitcamp.model.Contact;

public class ApplicationView extends Main {

	public static void home() {
		JPanel content = new JPanel();
		
		JLabel greeting = new JLabel("Welcome to BitBook");
		Font greetingFont = new Font("SansSerif", Font.BOLD, 30);
		greeting.setFont(greetingFont);
		content.add(greeting);
		
		JButton showContacts = new JButton("Show Contacts");
		showContacts.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// ContactController.list();
			}

		});

		JButton addContact = new JButton("Add Contact");
		addContact.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// ContactController.newContact();
			}
		});
		
		content.add(addContact);
		content.add(showContacts);
		Main.replaceContent(content);
	}

}
