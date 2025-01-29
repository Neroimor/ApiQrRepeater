package com.neroimor.QRrepeater.Controllers;

import com.google.zxing.WriterException;
import com.neroimor.QRrepeater.Services.GenerateQrCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;

@RestController
@Slf4j
@RequestMapping("/generate")
public class QRGeneraterController {


    private final GenerateQrCode generateQrCode;

    public QRGeneraterController(GenerateQrCode generateQrCode) {
        this.generateQrCode = generateQrCode;
    }

    @GetMapping(value = "/qr/{user}", produces = MediaType.IMAGE_PNG_VALUE)
    public @ResponseBody byte[] getUserQrCode(@PathVariable String user,
                                              @RequestParam(value = "repurl") String repurl) throws WriterException, IOException {
        log.info("Generate QR Code for user {}", user);
        return generateQrCode.generateQrCode(user,repurl);
    }

    @GetMapping("/rep/{user}")
    public RedirectView repeaterURL(@PathVariable String user){
        log.info("Transition to another page");
        return new RedirectView(generateQrCode.getUrlQrCode(user));
    }
    @GetMapping("/count/{user}")
    public String getCounter(@PathVariable String user){
        log.info("Count user {}", user);
        return String.valueOf(generateQrCode.getCounterQrCode(user));
    }
    @DeleteMapping("/delete/{user}")
    public void deleteQrCode(@PathVariable String user){
        generateQrCode.deleteQrCode(user);
    }
    @GetMapping("/notFound")
    public String repeaterURL(){
        log.info("not found qr code");
        return "not found";
    }
}
