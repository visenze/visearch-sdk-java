package com.visenze.visearch.internal;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.visenze.visearch.*;
import com.visenze.visearch.internal.http.ViSearchHttpClient;
import org.imgscalr.Scalr;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class SearchOperationsImpl extends BaseViSearchOperations implements SearchOperations {

    public SearchOperationsImpl(ViSearchHttpClient viSearchHttpClient, ObjectMapper objectMapper, String endpoint) {
        super(viSearchHttpClient, objectMapper, endpoint);
    }

    @Override
    public PagedSearchResult<ImageResult> search(SearchParams searchParams) {
        String imageId = searchParams.getImName();
        if (imageId == null || imageId.isEmpty()) {
            throw new ViSearchException("Missing parameter");
        }
        String response = viSearchHttpClient.get(endpoint + "/search", searchParams.toMap());
        return getPagedResult(response);
    }

    @Override
    public PagedSearchResult<ImageResult> colorSearch(ColorSearchParams colorSearchParams) {
        String color = colorSearchParams.getColor();
        if (color == null || color.isEmpty()) {
            throw new ViSearchException("Missing parameter");
        }
        if (!color.matches("^[0-9a-fA-F]{6}$")) {
            throw new ViSearchException("Invalid parameter");
        }
        String response = viSearchHttpClient.get(endpoint + "/colorsearch", colorSearchParams.toMap());
        return getPagedResult(response);
    }

    @Override
    public PagedSearchResult<ImageResult> uploadSearch(UploadSearchParams uploadSearchParams) {
        return uploadSearch(uploadSearchParams, ResizeSettings.STANDARD);
    }

    @Override
    public PagedSearchResult<ImageResult> uploadSearch(UploadSearchParams uploadSearchParams, ResizeSettings resizeSettings) {
        File imageFile = uploadSearchParams.getImageFile();
        String imageUrl = uploadSearchParams.getImageUrl();
        String response;
        if (imageFile == null && (imageUrl == null || imageUrl.isEmpty())) {
            throw new ViSearchException("Missing image parameter for upload search");
        } else if (imageFile != null) {
            byte[] resizedImageBytes = resizeImage(uploadSearchParams, imageFile, resizeSettings);
            response = viSearchHttpClient.postImage(endpoint + "/uploadsearch", uploadSearchParams.toMap(), resizedImageBytes, imageFile.getName());
        } else {
            response = viSearchHttpClient.post(endpoint + "/uploadsearch", uploadSearchParams.toMap());
        }
        return getPagedResult(response);
    }

    private byte[] resizeImage(UploadSearchParams uploadSearchParams, File imageFile, ResizeSettings resizeSettings) {
        try {
            BufferedImage sourceImage =  ImageIO.read(imageFile);
            int imageWidth = sourceImage.getWidth();
            int imageHeight = sourceImage.getHeight();
            BufferedImage resizedImage;
            if (imageWidth > resizeSettings.getWidth() && imageHeight > resizeSettings.getHeight()) {
                if (imageWidth >= imageHeight) {
                    // landscape or square image
                    resizedImage = Scalr.resize(sourceImage, Scalr.Mode.FIT_TO_HEIGHT, resizeSettings.getHeight());
                } else {
                    // portrait image
                    resizedImage = Scalr.resize(sourceImage, Scalr.Mode.FIT_TO_WIDTH, resizeSettings.getWidth());
                }
            } else {
                resizedImage = sourceImage;
            }
            scaleImageBox(uploadSearchParams, sourceImage, resizedImage);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            writeToOutputStream(outputStream, resizeSettings, resizedImage);
            sourceImage.flush();
            resizedImage.flush();
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new ViSearchException("Could not open image file: " + e.getMessage());
        }
    }

    private void scaleImageBox(UploadSearchParams uploadSearchParams, BufferedImage sourceImage, BufferedImage resizedImage) {
        if (uploadSearchParams.getBox() != null) {
            Box box = uploadSearchParams.getBox();
            if (box.allCoordsExist()) {
                int imageWidth = sourceImage.getWidth();
                int resizedWidth = resizedImage.getWidth();
                float ratio = resizedWidth / (float) imageWidth;
                box.scale(ratio);
            }
        }
    }

    private void writeToOutputStream(OutputStream outputStream, ResizeSettings resizeSettings, BufferedImage resizedImage) throws IOException {
        ImageOutputStream imageOutputStream = ImageIO.createImageOutputStream(outputStream);
        Iterator<ImageWriter> it = ImageIO.getImageWritersByFormatName("jpeg");
        ImageWriter writer = it.next();
        ImageWriteParam param = writer.getDefaultWriteParam();
        param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        param.setCompressionQuality(resizeSettings.getQuality() / 100.0f);
        writer.setOutput(imageOutputStream);
        writer.write(null, new IIOImage(resizedImage, null, null), param);
        writer.dispose();
    }

    private PagedSearchResult<ImageResult> getPagedResult(String json) {
        JsonNode node;
        try {
            node = objectMapper.readTree(json);
        } catch (Exception e) {
            throw new ViSearchException("Error deserializing json=" + json);
        }
        checkStatus(node);
        PagedResult<ImageResult> pagedResult = pagify(json, ImageResult.class);
        PagedSearchResult<ImageResult> result = new PagedSearchResult<ImageResult>(pagedResult);
        JsonNode facetsNode = node.get("facets");
        if (facetsNode != null) {
            List<Facet> facets = deserializeListResult(facetsNode, Facet.class);
            result.setFacets(facets);
        }
        JsonNode qinfoNode = node.get("qinfo");
        if (qinfoNode != null) {
            try {
                Map<String, String> qinfo = deserializeMapResult(qinfoNode, String.class, String.class);
                result.setQueryInfo(qinfo);
            } catch (Exception e) {
                throw new ViSearchException("Error deserializing qinfo");
            }
        }
        result.setRawJson(node.toString());
        return result;
    }

    private void checkStatus(JsonNode node) {
        JsonNode statusNode = node.get("status");
        if (statusNode == null) {
            throw new ViSearchException("Error receiving api response");
        } else {
            String status = statusNode.asText();
            if (!status.equals("OK")) {
                JsonNode errorNode = node.get("error");
                if (errorNode == null) {
                    throw new ViSearchException("Error receiving api response");
                }
                String errorMessage = errorNode.path(0).asText();
                throw new ViSearchException("Error : " + errorMessage, node.toString());
            }
        }
    }
}
