package com.visenze.visearch.internal;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import com.visenze.visearch.*;
import com.visenze.visearch.internal.http.ViSearchHttpClient;
import org.imgscalr.Scalr;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class SearchOperationsImpl extends BaseViSearchOperations implements SearchOperations {

    public SearchOperationsImpl(ViSearchHttpClient viSearchHttpClient, ObjectMapper objectMapper) {
        super(viSearchHttpClient, objectMapper);
    }

    @Override
    public PagedSearchResult search(SearchParams searchParams) {
        String response = viSearchHttpClient.get("/search", searchParams.toMap());
        return getPagedResult(response);
    }

    @Override
    public PagedSearchResult colorSearch(ColorSearchParams colorSearchParams) {
        String response = viSearchHttpClient.get("/colorsearch", colorSearchParams.toMap());
        return getPagedResult(response);
    }

    @Override
    public PagedSearchResult uploadSearch(UploadSearchParams uploadSearchParams) {
        return uploadSearch(uploadSearchParams, ResizeSettings.STANDARD);
    }

    @Override
    public PagedSearchResult uploadSearch(UploadSearchParams uploadSearchParams, ResizeSettings resizeSettings) {
        File imageFile = uploadSearchParams.getImageFile();
        InputStream imageStream = uploadSearchParams.getImageStream();
        String imageUrl = uploadSearchParams.getImageUrl();
        String response;
        if (imageFile == null && imageStream == null && (Strings.isNullOrEmpty(imageUrl))) {
            throw new IllegalArgumentException("Must provide either an image file or an image url to perform upload search");
        } else if (imageFile != null) {
            byte[] imageBytes = resizeImage(uploadSearchParams, imageFile, resizeSettings);
            response = viSearchHttpClient.postImage("/uploadsearch", uploadSearchParams.toMap(), imageBytes, imageFile.getName());
        } else if (imageStream != null) {
            byte[] imageBytes = resizeImage(uploadSearchParams, imageStream, resizeSettings);
            response = viSearchHttpClient.postImage("/uploadsearch", uploadSearchParams.toMap(), imageBytes, "image-stream");
        } else {
            response = viSearchHttpClient.post("/uploadsearch", uploadSearchParams.toMap());
        }
        return getPagedResult(response);
    }

    private byte[] resizeImage(UploadSearchParams uploadSearchParams, File imageFile, ResizeSettings resizeSettings) {
        try {
            return resizeImage(uploadSearchParams, new FileInputStream(imageFile), resizeSettings);
        } catch (FileNotFoundException e) {
            throw new ViSearchException("Could not found the image file " + imageFile.getAbsolutePath(), e);
        }
    }

    private byte[] resizeImage(UploadSearchParams uploadSearchParams, InputStream inputStream, ResizeSettings resizeSettings) {
        try {
            BufferedImage sourceImage = ImageIO.read(inputStream);
            int imageWidth = sourceImage.getWidth();
            int imageHeight = sourceImage.getHeight();
            BufferedImage resizedImage;
            if (imageWidth > resizeSettings.getWidth() && imageHeight > resizeSettings.getHeight()) {
                resizedImage = Scalr.resize(sourceImage, resizeSettings.getWidth());
            } else {
                resizedImage = sourceImage;
            }
            scaleImageBox(uploadSearchParams, sourceImage, resizedImage);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            writeToOutputStream(outputStream, resizeSettings, resizedImage);
            sourceImage.flush();
            resizedImage.flush();
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new ViSearchException("Could not read the image from input stream.", e);
        }
    }

    private void scaleImageBox(UploadSearchParams uploadSearchParams, BufferedImage sourceImage, BufferedImage resizedImage) {
        if (uploadSearchParams.getBox() != null) {
            Box box = uploadSearchParams.getBox();
            if (box.allCoordsExist()) {
                int imageWidth = sourceImage.getWidth();
                int resizedWidth = resizedImage.getWidth();
                float ratio = resizedWidth / (float) imageWidth;
                uploadSearchParams.setBox(box.scale(ratio));
            }
        }
    }

    private void writeToOutputStream(OutputStream outputStream, ResizeSettings resizeSettings, BufferedImage resizedImage) {
        try {
            ImageOutputStream imageOutputStream = ImageIO.createImageOutputStream(outputStream);
            Iterator<ImageWriter> it = ImageIO.getImageWritersByFormatName("jpeg");
            ImageWriter writer = it.next();
            ImageWriteParam param = writer.getDefaultWriteParam();
            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            param.setCompressionQuality(resizeSettings.getQuality() / 100.0f);
            writer.setOutput(imageOutputStream);
            writer.write(null, new IIOImage(resizedImage, null, null), param);
            writer.dispose();
        } catch (IOException e) {
            throw new ViSearchException("Could not write to buffer when scaling image before upload search. " +
                    "Please ensure sufficient memory for your runtime and try again.", e);
        }
    }

    private PagedSearchResult getPagedResult(String response) {
        JsonNode node;
        try {
            node = objectMapper.readTree(response);
        } catch (JsonProcessingException e) {
            throw new ViSearchException("Could not parse the ViSearch response: " + response, e, response);
        } catch (IOException e) {
            throw new ViSearchException("Could not parse the ViSearch response: " + response, e, response);
        }
        checkResponseStatus(node);

        PagedResult<ImageResult> pagedResult = pagify(response, ImageResult.class);
        PagedSearchResult result = new PagedSearchResult(pagedResult);
        JsonNode facetsNode = node.get("facets");
        if (facetsNode != null) {
            List<Facet> facets = deserializeListResult(facetsNode, Facet.class);
            result.setFacets(facets);
        }
        JsonNode qinfoNode = node.get("qinfo");
        if (qinfoNode != null) {
            Map<String, String> qinfo = deserializeMapResult(qinfoNode, String.class, String.class);
            result.setQueryInfo(qinfo);
        }
        result.setRawJson(node.toString());
        return result;
    }

    private void checkResponseStatus(JsonNode node) {
        String json = node.toString();
        JsonNode statusNode = node.get("status");
        if (statusNode == null) {
            throw new ViSearchException("There was a malformed ViSearch response: " + json, json);
        } else {
            String status = statusNode.asText();
            if (!status.equals("OK")) {
                JsonNode errorNode = node.get("error");
                if (errorNode == null) {
                    throw new ViSearchException("An unknown error occurred in ViSearch: " + json, json);
                }
                String message = errorNode.path(0).asText();
                throw new ViSearchException("An error occurred calling ViSearch: " + message, json);
            }
        }
    }
}
