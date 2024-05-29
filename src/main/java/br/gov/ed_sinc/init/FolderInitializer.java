package br.gov.ed_sinc.init;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.PosixFileAttributeView;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.EnumSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class FolderInitializer {

    @Value("/home/edsinc/assetstore/imagens")
    private String folderPath;

    @PostConstruct
    public void init() {
        File folder = new File(folderPath);

        if (!folder.exists()) {
            if (folder.mkdirs()) {
                System.out.println("Pasta criada em: " + folderPath);

                setPermissions(folder.toPath());
            } else {
                System.err.println("Falha ao criar a pasta em: " + folderPath);
            }
        }
    }

    private static void setPermissions(Path folderPath) {
        try {
            Files.walkFileTree(folderPath, EnumSet.noneOf(FileVisitOption.class), Integer.MAX_VALUE, new PermissionVisitor());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class PermissionVisitor extends SimpleFileVisitor<Path> {
        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            PosixFileAttributeView view = Files.getFileAttributeView(file, PosixFileAttributeView.class);
            if (view != null) {
                Set<PosixFilePermission> permissions = PosixFilePermissions.fromString("rwxr-xr--");
                view.setPermissions(permissions);
            }
            return FileVisitResult.CONTINUE;
        }
    }
}
