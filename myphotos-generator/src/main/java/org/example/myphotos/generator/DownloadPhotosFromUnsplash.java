package org.example.myphotos.generator;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;

import javax.ws.rs.client.*;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;

public class DownloadPhotosFromUnsplash {
    public static void main(String[] args) throws IOException {
        new DownloadPhotosFromUnsplash()
                .execute("https://api.unsplash.com/photos?per_page=30", Paths.get("external/test-data/photos"));

    }

    public void execute(String sourceUnsplashApiUrl, Path destinationDirectoryPath) throws IOException {
        this.createDestinationDirectoryIfNecessary(destinationDirectoryPath);
        Response responseWithPhotoLinksJson = getPhotoLinks(sourceUnsplashApiUrl);
        if (responseWithPhotoLinksJson.getStatus() == Response.Status.OK.getStatusCode()) {
            parseValidResponse(responseWithPhotoLinksJson, destinationDirectoryPath);
        } else {
            displayErrorMessage(responseWithPhotoLinksJson);
        }
    }

    protected void createDestinationDirectoryIfNecessary(Path destinationDirectoryPath) throws IOException {
        if (!Files.exists(destinationDirectoryPath)) {
            Files.createDirectories(destinationDirectoryPath);
        }
    }

    protected void parseValidResponse(Response responseWithPhotoLinksJson, Path destinationDirectoryPath) {
        System.out.println("X-Ratelimit-Remaining=" + responseWithPhotoLinksJson.getHeaderString("X-Ratelimit-Remaining"));
        List<Item> itemsPhotoLinks = responseWithPhotoLinksJson.readEntity(new GenericType<List<Item>>() {
        });
        int id = 10;
        for (Item itemPhotoLinks : itemsPhotoLinks) {
            Path pathFile = Paths.get(String.format("%s/%s.jpg", destinationDirectoryPath.toAbsolutePath().toString(), id));
            this.downloadImage(itemPhotoLinks.getLinks().getDownload(), pathFile);
            id++;
            System.out.println("Successful downloaded " + itemPhotoLinks.getLinks().getDownload());
        }
    }

    protected void downloadImage(String downloadUrl, Path targetPathFile) {
        Response responseWithImage = ClientBuilder.newClient()
                .register((ClientResponseFilter) (requestContext, responseContext) -> {
                    if (responseContext.getStatusInfo().getFamily() == Response.Status.Family.REDIRECTION) {
                        Response response = requestContext.getClient()
                                .target(responseContext.getLocation())
                                .request()
                                .method(requestContext.getMethod());
                        responseContext.setEntityStream((InputStream) response.getEntity());
                        responseContext.setStatusInfo(response.getStatusInfo());
                        responseContext.setStatus(response.getStatus());
                    }
                })
                .target(downloadUrl)
                .request("image/jpeg")
                .get();
        try (InputStream inputStream = responseWithImage.readEntity(InputStream.class)) {
            Files.copy(inputStream, targetPathFile, StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            System.err.println("Download file failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    protected void displayErrorMessage(Response responseWithPhotoLinksJson) {
        System.out.println(String.format("Status: %s %s",
                responseWithPhotoLinksJson.getStatusInfo().getStatusCode(),
                responseWithPhotoLinksJson.getStatusInfo().getReasonPhrase()));
    }

    protected Response getPhotoLinks(String sourceUnsplashApiUrl) {
        Client client = ClientBuilder.newClient();
        ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        client.register(new JacksonJaxbJsonProvider(objectMapper, JacksonJaxbJsonProvider.DEFAULT_ANNOTATIONS));

        return client.target(sourceUnsplashApiUrl)
                .request(MediaType.APPLICATION_JSON)
                .header("Accept_Version", "v1")
                .header("Authorization", "Client-ID " + getSystemEnvironmentVariable("UNSPLASH_KEY"))
                .get();
    }

    protected String getSystemEnvironmentVariable(String nameOfVariable) {
        Map<String, String> systemEnv = System.getenv();
        for (Map.Entry<String, String> nameAndValueEntryEnvVariable : systemEnv.entrySet()) {
            if (nameAndValueEntryEnvVariable.getKey().equals(nameOfVariable)) {
                return nameAndValueEntryEnvVariable.getValue();
            }
        }
        throw new IllegalStateException("System variable not defined: " + nameOfVariable);
    }

    private static class Item {
        private Links links;

        public Links getLinks() {
            return links;
        }

        public void setLinks(Links links) {
            this.links = links;
        }
    }

    private static class Links {

        private String download;

        public String getDownload() {
            return download;
        }

        public void setDownload(String download) {
            this.download = download;
        }

    }
}


