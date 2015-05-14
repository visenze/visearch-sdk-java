package com.visenze.visearch.internal;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import com.twelvemonkeys.image.ResampleOp;
import com.visenze.visearch.*;
import com.visenze.visearch.internal.http.ViSearchHttpClient;

import javax.imageio.*;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
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
            throw new IllegalArgumentException("Must provide either an image file, input stream, or an image url to perform upload search");
        } else if (imageFile != null) {
            InputStream inputStream = resizeImageAndBox(uploadSearchParams, imageFile, resizeSettings);
            response = viSearchHttpClient.postImage("/uploadsearch", uploadSearchParams.toMap(), inputStream, imageFile.getName());
        } else if (imageStream != null) {
            InputStream inputStream = resizeImageAndBox(uploadSearchParams, imageStream, resizeSettings);
            response = viSearchHttpClient.postImage("/uploadsearch", uploadSearchParams.toMap(), inputStream, "image-stream");
        } else {
            response = viSearchHttpClient.post("/uploadsearch", uploadSearchParams.toMap());
        }
        return getPagedResult(response);
    }

    private InputStream resizeImageAndBox(UploadSearchParams uploadSearchParams, File imageFile, ResizeSettings resizeSettings) {
        try {
            ImageInputStream imageInputStream = ImageIO.createImageInputStream(imageFile);
            return processImageInputStream(uploadSearchParams, imageInputStream, resizeSettings);
        } catch (IOException e) {
            throw new ViSearchException("Caught IOException while processing the image file " + imageFile.getAbsolutePath(), e);
        } catch (IllegalArgumentException e) {
            throw new ViSearchException("The image file seems to be invalid");
        } catch (Exception e) {
            throw new ViSearchException("Could not process the image file", e);
        }
    }

    private InputStream resizeImageAndBox(UploadSearchParams uploadSearchParams, InputStream imageStream, ResizeSettings resizeSettings) {
        try {
            ImageInputStream imageInputStream = ImageIO.createImageInputStream(imageStream);
            return processImageInputStream(uploadSearchParams, imageInputStream, resizeSettings);
        } catch (IOException e) {
            throw new ViSearchException("Caught IOException while processing the image stream", e);
        } catch (IllegalArgumentException e) {
            throw new ViSearchException("The image stream seems to be invalid");
        } catch (Exception e) {
            throw new ViSearchException("Could not process the image stream", e);
        }
    }

    private InputStream processImageInputStream(UploadSearchParams uploadSearchParams, ImageInputStream imageInputStream, ResizeSettings resizeSettings)
            throws IOException {
        BufferedImage origImage = readImage(imageInputStream);
        BufferedImage scaledImage = scaleImage(origImage, resizeSettings.getWidth(), resizeSettings.getHeight());
        scaleBox(uploadSearchParams, origImage, scaledImage);
        BufferedImage rgbImage = getRGBImage(scaledImage);
        return getJPGStream(rgbImage, resizeSettings.getQuality());
    }

    private BufferedImage readImage(ImageInputStream imageInputStream) throws IOException {
        try {
            Iterator<ImageReader> readers = ImageIO.getImageReaders(imageInputStream);
            if (!readers.hasNext()) {
                throw new IllegalArgumentException("No reader for the image stream");
            }
            ImageReader imageReader = readers.next();
            try {
                imageReader.setInput(imageInputStream);
                ImageReadParam param = imageReader.getDefaultReadParam();
                return imageReader.read(0, param);
            } finally {
                imageReader.dispose();
            }
        } finally {
            imageInputStream.close();
        }
    }

    private BufferedImage scaleImage(BufferedImage origImage, int targetWidth, int targetHeight) {
        int origWidth = origImage.getWidth();
        int origHeight = origImage.getHeight();
        BufferedImage outputImage;
        if (origWidth > targetWidth || origHeight > targetHeight) {
            int resizedWidth;
            int resizedHeight;
            double origRatio = ((double) origWidth) / origHeight;
            double targetRatio = ((double) targetWidth) / targetHeight;
            if (targetRatio < origRatio) {
                resizedWidth = targetWidth;
                resizedHeight = (int) Math.round(targetWidth / origRatio);
            } else {
                resizedWidth = (int) Math.round(targetHeight * origRatio);
                resizedHeight = targetHeight;
            }
            outputImage = resampleImage(origImage, resizedWidth, resizedHeight);
        } else {
            outputImage = origImage;
        }
        return outputImage;
    }

    private static BufferedImage resampleImage(BufferedImage origImage, int width, int height) {
        BufferedImageOp resampler = new ResampleOp(width, height, ResampleOp.FILTER_LANCZOS);
        return resampler.filter(origImage, null);
    }

    private void scaleBox(UploadSearchParams uploadSearchParams, BufferedImage origImage, BufferedImage scaledImage) {
        float origWidth = origImage.getWidth();
        float scaledWidth = scaledImage.getWidth();
        if (uploadSearchParams.getBox() != null) {
            Box box = uploadSearchParams.getBox();
            if (box.allCoordsExist()) {
                float ratio = scaledWidth / origWidth;
                uploadSearchParams.setBox(box.scale(ratio));
            }
        }
    }

    private BufferedImage getRGBImage(BufferedImage bufferedImage) {
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        BufferedImage rgbImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int pixel = bufferedImage.getRGB(x, y);
                rgbImage.setRGB(x, y, pixel);
            }
        }
        return rgbImage;
    }

    private InputStream getJPGStream(BufferedImage rgbImage, float quality) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            writeJPGStream(rgbImage, outputStream, quality);
            return new ByteArrayInputStream(outputStream.toByteArray());
        } finally {
            outputStream.close();
        }
    }

    private void writeJPGStream(BufferedImage bufferedImage, OutputStream outputStream, float quality) throws IOException {
        Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpg");
        if (!writers.hasNext()) {
            throw new IllegalArgumentException("No writer for jpg format");
        }
        ImageWriter imageWriter = writers.next();
        try {
            ImageOutputStream imageOutputStream = ImageIO.createImageOutputStream(outputStream);
            try {
                imageWriter.setOutput(imageOutputStream);
                ImageWriteParam param = imageWriter.getDefaultWriteParam();
                param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                param.setCompressionQuality(quality);
                imageWriter.write(null, new IIOImage(bufferedImage, null, null), param);
            } finally {
                imageOutputStream.close();
            }
        } finally {
            imageWriter.dispose();
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
