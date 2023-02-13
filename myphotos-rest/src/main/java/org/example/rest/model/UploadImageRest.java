package org.example.rest.model;

import org.apache.commons.fileupload.FileItem;
import org.example.common.model.AbstractMimeTypeImageResourceImpl;
import org.example.model.model.ImageResource;

import java.io.IOException;

public class UploadImageRest {

    private final FileItem fileItem;

    public UploadImageRest(FileItem fileItem) {
        this.fileItem = fileItem;
    }

    public ImageResource getImageResource() {
        return new FileItemImageResource();
    }

    private class FileItemImageResource extends AbstractMimeTypeImageResourceImpl {
        @Override
        protected void copyContent() throws Exception {
            fileItem.write(this.getTempPath().toFile());
        }

        @Override
        protected String getContentType() {
            return fileItem.getContentType();
        }

        @Override
        protected void deleteTempResource() throws IOException {
            fileItem.delete();
        }

        @Override
        public String toString() {
            return String.format("%s(%s)", this.getClass().getSimpleName(), fileItem);
        }
    }
}
