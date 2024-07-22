package com.unla.eventos.services.implementation;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Service
public class QRCodeService {

    @Value("${PUBLIC_QR_LINK_SERVER}")
    private String PUBLIC_QR_LINK_SERVER;
    
    public byte[] generateQRCodeBytes(String code, int width, int height) throws WriterException, IOException {
        InputStream logoStream = getClass().getResourceAsStream("/static/images/logo_small.png");

        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        String qrText = PUBLIC_QR_LINK_SERVER + code;
        BitMatrix bitMatrix = qrCodeWriter.encode(qrText, BarcodeFormat.QR_CODE, width, height, hints);

        BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
        BufferedImage logoImage = ImageIO.read(logoStream);

        // Calculate the center position to place the logo
        int deltaHeight = qrImage.getHeight() - logoImage.getHeight();
        int deltaWidth = qrImage.getWidth() - logoImage.getWidth();

        // Create a new buffered image with the same dimensions as the QR code
        BufferedImage combined = new BufferedImage(qrImage.getWidth(), qrImage.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g = combined.createGraphics();
        
        // Draw the QR code image
        g.drawImage(qrImage, 0, 0, null);
        
        // Draw the logo image on top of the QR code
        g.drawImage(logoImage, deltaWidth / 2, deltaHeight / 2, null);
        g.dispose();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(combined, "PNG", outputStream);
        return outputStream.toByteArray();
    }
}
