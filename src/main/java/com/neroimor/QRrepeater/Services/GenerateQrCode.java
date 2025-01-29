package com.neroimor.QRrepeater.Services;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.neroimor.QRrepeater.DAO.Model.UsersQR;
import com.neroimor.QRrepeater.DAO.Repositorys.UserQRRepositroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

@Service
@Slf4j
public class GenerateQrCode {
    @Value("${URL.name.qr}")
    private String pathURL;
    @Value("${URL.name.notFound}")
    private String pathNotFound;


    private final UserQRRepositroy userQRRepositroy;

    public GenerateQrCode(UserQRRepositroy userQRRepositroy) {
        this.userQRRepositroy = userQRRepositroy;
    }

    public byte[] generateQrCode(String name, String repUrl) throws WriterException, IOException {
        name = checkName(name);
        String data = pathURL +"/"+ name;
        Map<EncodeHintType,Object> hintMap = new HashMap<>();
        hintMap.put(EncodeHintType.MARGIN, 1);
        hintMap.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        BitMatrix matrix = new MultiFormatWriter()
                .encode(data, BarcodeFormat.QR_CODE, 200,200 ,hintMap);
        BufferedImage image = new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < 200; i++) {
            for (int j = 0; j < 200; j++) {
                image.setRGB(i, j, matrix.get(i, j) ? Color.BLACK.getRGB() : Color.WHITE.getRGB());
            }
        }
        addedQrToDB(name,data,repUrl);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);
        return baos.toByteArray();
    }

    private String checkName(String name){
        Optional<UsersQR> usersQR = userQRRepositroy.findByName(name);
        if(usersQR.isPresent()){
            String newName="";
            while (true){
                Random rand = new Random();
                 newName = usersQR.get().getName() + rand.nextInt(1000000);
                 log.info("New Name : "+newName);
                if (userQRRepositroy.findByName(newName).isEmpty()){
                    break;
                }
            }
            return newName;
        }
        else{
            return name;
        }

    }


    private void addedQrToDB(String name,String url, String repUrl){
        var user = new UsersQR();
        user.setName(name);
        user.setUrl(url);
        user.setTranurl(repUrl);
        user.setCounter(0);
        userQRRepositroy.save(user);
    }

    public String getUrlQrCode(String name){
        return userQRRepositroy.findByName(name)
                .map(user -> {
                    log.info( user.getUrl());
                    user.setCounter(user.getCounter() + 1);
                    userQRRepositroy.save(user);
                    return user.getTranurl();
                })
                .orElse(pathNotFound);
    }
}
