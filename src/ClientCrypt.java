import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.X509EncodedKeySpec;

public class ClientCrypt {
    public static void main(String[] args) throws Exception{
        System.out.println("Client> Waiting for public key...");
        System.in.read();
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextInt();
        keyGenerator.init(secureRandom);
        // симметричный ключ
        SecretKey secretKey = keyGenerator.generateKey();
        Cipher cipher = Cipher.getInstance("RSA");
        Path pathToKey = Paths.get("C:\\Users\\Silence\\Desktop\\publicKey.dat");
        // паблик ключ из серверной пары ключей
        byte[] publicKeyBytes = Files.readAllBytes(pathToKey);
        PublicKey publicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(publicKeyBytes));
        cipher.init(Cipher.WRAP_MODE, publicKey);
        byte[] cryptedKey = cipher.wrap(secretKey);
        Path pathToCryptedKey = Paths.get("C:\\Users\\Silence\\Desktop\\cryptedKey.dat");
        Files.write(pathToCryptedKey, cryptedKey);
        System.out.println("Client> Encrypted Secret Key has been generated. Waiting...");
        System.in.read();

        Path pathToMessage = Paths.get("C:\\Users\\Silence\\Desktop\\message.dat");
        byte[] cryptedMessage = Files.readAllBytes(pathToMessage);
        byte[] decryptedText = new byte[cryptedMessage.length];
        System.out.println("Client> Encrypted message is: " + new String(cryptedMessage));
        cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        int ptLength = cipher.update(cryptedMessage, 0, cryptedMessage.length, decryptedText, 0);
        ptLength += cipher.doFinal(decryptedText, ptLength);
        System.out.println("Client> Message is: " + new String(decryptedText));
    }
}
