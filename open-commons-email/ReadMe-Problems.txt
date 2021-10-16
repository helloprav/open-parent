
More Info:
 - https://stackoverflow.com/questions/33123271/how-to-make-java-string-colored-when-sending-mail-using-simplemailmessage-class
	- The Spring 4.2.1 (http://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/mail/SimpleMailMessage.html) documentation states, 
		that you should use MimeMessage instead of SimpleMailMessage if you want colors.
	- Limitation: <fromAddress>/<fromName> is not supported, configured username becomes fromAddress: https://stackoverflow.com/questions/32941673/senders-address-displayed-rather-than-his-name-while-sending-with-simplemailmes
