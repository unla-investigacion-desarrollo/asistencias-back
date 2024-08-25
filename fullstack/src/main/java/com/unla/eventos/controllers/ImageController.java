package com.unla.eventos.controllers;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import com.unla.eventos.helpers.ViewRouteHelper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


@Controller
public class ImageController {

    @GetMapping("/images/events/{filename}")
    @ResponseBody
    public ResponseEntity<Resource> serveImage(@PathVariable String filename) throws IOException {
        Path imagePath = Paths.get(ViewRouteHelper.UPLOADS_IMAGES_EVENTS).resolve(filename);
        Resource image = new UrlResource(imagePath.toUri());

        if (image.exists() || image.isReadable()) {
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, Files.probeContentType(imagePath))
                    .body(image);
        } else {
            throw new RuntimeException("Could not read the file!");
        }
    }
}

