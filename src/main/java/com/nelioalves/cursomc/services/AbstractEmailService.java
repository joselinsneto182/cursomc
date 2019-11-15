package com.nelioalves.cursomc.services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.nelioalves.cursomc.domain.Cliente;
import com.nelioalves.cursomc.domain.Pedido;

public abstract class AbstractEmailService implements EmailService{
	
	@Value("${default.sender}")
	private String sender;
	
	@Autowired
	private TemplateEngine templateEngine;
	
//	@Autowired
//	private JavaMailSender javaMailSender;
	
	@Override
	public void sendOrderConfirmationEmail(Pedido pedido) {
		SimpleMailMessage msg = prepareSimpleMailMessageFromPedido(pedido);
		sendEmail(msg);
	}
	
	protected SimpleMailMessage prepareSimpleMailMessageFromPedido(Pedido pedido) {
		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setTo(pedido.getCliente().getEmail());
		msg.setFrom(sender);
		msg.setSubject("Pedido confirmado! Código: "+pedido.getId());
		msg.setText(pedido.toString());
		msg.setSentDate(new Date(System.currentTimeMillis()));
		return msg;
	}
	
	protected String htmlFromTemplatePedido(Pedido obj) {
		Context context = new Context();
		context.setVariable("pedido", obj);
		return templateEngine.process("email/confirmacaoPedido", context);
	}
	
//	protected MimeMessage prepareMimeMessageFromPedido(Pedido pedido) throws MessagingException {
//		MimeMessage mm = javaMailSender.createMimeMessage();
//		MimeMessageHelper mmh = new MimeMessageHelper(mm, true);
//		mmh.setTo(pedido.getCliente().getEmail());
//		mmh.setFrom(sender);
//		mmh.setSubject("Pedido confirmado! Código: "+pedido.getId());
//		mmh.setText(htmlFromTemplatePedido(pedido),true);
//		mmh.setSentDate(new Date(System.currentTimeMillis()));
//		return mm;
//	}
	
//	@Override
//	public void sendOrderConfirmationHtmlEmail(Pedido pedido) {
//		try {
//			MimeMessage mm = prepareMimeMessageFromPedido(pedido);
//			sendHtmlEmail(mm);
//		}catch(MessagingException e) {
//			sendOrderConfirmationEmail(pedido);
//		}
//	}
	
	@Override
	public void sendNewPasswordEmail(Cliente cliente, String newPass) {
		SimpleMailMessage msg = prepareNewPasswordEmail(cliente, newPass);
		sendEmail(msg);
	}

	private SimpleMailMessage prepareNewPasswordEmail(Cliente cliente, String newPass) {
		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setTo(cliente.getEmail());
		msg.setFrom(sender);
		msg.setSubject("Solicitação de nova senha");
		msg.setSentDate(new Date(System.currentTimeMillis()));
		msg.setText("Nova senha: "+newPass);
		return msg;
	}
	
}
