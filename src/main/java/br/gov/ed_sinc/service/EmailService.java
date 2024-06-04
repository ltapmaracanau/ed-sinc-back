package br.gov.ed_sinc.service;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import br.gov.ed_sinc.model.EmailDetails;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

	@Autowired
	private JavaMailSender javaMailSender;

	@Value("${spring.mail.username}")
	private String sender;
	
    public ResponseEntity<Void> enviarEmailRecuperacaoSenha(String email, String token, String linkResetSenha) {
        try {
            String subject = "Redefinição de senha ed-sinc";
            
            String htmlContent = "<html><body style='text-align: center;'>"
                    + "<p style='font-size: 20px;'>Prezado(a),</p>"
                    + "<p style='font-size: 16px;'>Você solicitou a redefinição da sua senha. Utilize o Token a seguir para continuar a alteração:</p>"
                    + "<p style='font-size: 20px;'><strong style='animation: bounce 1s infinite;'>" + token + "</strong></p>"
                    + "<p style='font-size: 16px;'>Caso você não tenha feito essa solicitação, por favor ignore este e-mail.</p>"
                    + "<p style='font-size: 16px;'>Mensagem gerada automaticamente, não responda este e-mail.</p>"
                    + "</body>"
                    + "<style>"
                    + "@keyframes bounce { 0%, 100%, 20%, 50%, 80% {transform: translateY(0);} 40% {transform: translateY(-20px);} 60% {transform: translateY(-10px);}}"
                    + "</style>"
                    + "</html>";
            
            MimeMessage message = javaMailSender.createMimeMessage();
            
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(sender);
            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

            javaMailSender.send(message);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
	
    public ResponseEntity<Void> enviarEmailConfirmacaoCadastro(String email, String token, String linksetSenha) {
        try {
            String subject = "Conclusão de cadastro ed-sinc";
            String htmlContent = "<html><body style='text-align: center;'>"
                    + "<p style='font-size: 20px;'>Prezado(a),</p>"
                    + "<p style='font-size: 16px;'>Para concluir seu cadastro no ed-sinc, utilize o token abaixo: </p>"
                    + "<p style='font-size: 20px;'><strong style='animation: bounce 1s infinite;'>" + token + "</strong></p>"
                    + "<p style='font-size: 16px;'>Caso você não tenha feito essa solicitação, por favor ignore este e-mail.</p>"
                    + "<p style='font-size: 16px;'>Mensagem gerada automaticamente, não responda este e-mail.</p>"
                    + "</body>"
                    + "<style>"
                    + "@keyframes bounce { 0%, 100%, 20%, 50%, 80% {transform: translateY(0);} 40% {transform: translateY(-20px);} 60% {transform: translateY(-10px);}}"
                    + "</style>"
                    + "</html>";
            
            MimeMessage message = javaMailSender.createMimeMessage();
            
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(sender);
            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

            javaMailSender.send(message);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

	
	public String sendSimpleMail(EmailDetails details) {

		// Try block to check for exceptions
		try {

			// Creating a simple mail message
			SimpleMailMessage mailMessage = new SimpleMailMessage();

			// Setting up necessary details
			mailMessage.setFrom(sender);
			mailMessage.setTo(details.getRecipient());
			mailMessage.setText(details.getMsgBody());
			mailMessage.setSubject(details.getSubject());

			// Sending the mail
			javaMailSender.send(mailMessage);
			return "Mail Sent Successfully...";
		}

		// Catch block to handle the exceptions
		catch (Exception e) {
			System.out.println(e.getMessage());
			return "Error while Sending Mail";
		}
	}
	/*
	
	public ResponseEntity<String> enviarMensagemContato(MensagemContatoRequest request) {
	    try {
	        String subject = "Feedback aba Contato";

	        String htmlContent = "<html><body style='text-align: center;'>"
	                // ... (seu conteúdo HTML aqui)
	                + "</body>"
	                + "<style>"
	                + "@keyframes bounce { 0%, 100%, 20%, 50%, 80% {transform: translateY(0);} 40% {transform: translateY(-20px);} 60% {transform: translateY(-10px);}}"
	                + "<p style='font-size: 20px;'><strong style='animation: bounce 1s infinite;'>"+ request.getMensagem() +"</strong></p>"
	                + "</style>"
	                + "</html>";

	        MimeMessage message = javaMailSender.createMimeMessage();

	        MimeMessageHelper helper = new MimeMessageHelper(message, true);
	        helper.setFrom(sender);
	        helper.setTo("contatomaracaproj@gmail.com");
	        helper.setSubject(subject);
	        helper.setText(htmlContent, true);

	        javaMailSender.send(message);
	        
	        // Retorne um ResponseEntity com o código de status OK (200) e uma mensagem
	        return new ResponseEntity<>("Mail Sent Successfully", HttpStatus.OK);
	    } catch (Exception e) {
	        // Em caso de erro, retorne um ResponseEntity com o código de status de erro (por exemplo, 500) e a mensagem de erro
	        System.out.println(e.getMessage());
	        return new ResponseEntity<>("Error while Sending Mail", HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}
*/
	public String sendMailWithAttachment(EmailDetails details) {
		// Creating a mime message
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		MimeMessageHelper mimeMessageHelper;

		try {

			// Setting multipart as true for attachments to
			// be send
			mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
			mimeMessageHelper.setFrom(sender);
			mimeMessageHelper.setTo(details.getRecipient());
			mimeMessageHelper.setText(details.getMsgBody());
			mimeMessageHelper.setSubject(details.getSubject());

			// Adding the attachment
			FileSystemResource file = new FileSystemResource(new File(details.getAttachment()));

			mimeMessageHelper.addAttachment(file.getFilename(), file);

			// Sending the mail
			javaMailSender.send(mimeMessage);
			return "Mail sent Successfully";
		}

		// Catch block to handle MessagingException
		catch (MessagingException e) {

			// Display message when exception occurred
			return "Error while sending mail!!!";
		}
	}
}
