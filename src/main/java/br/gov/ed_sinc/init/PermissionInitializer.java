package br.gov.ed_sinc.init;

import java.io.IOException;
import java.nio.file.FileSystems;
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

public class PermissionInitializer {

    public static void setPermissions(String folderPath) {
        Path folder = FileSystems.getDefault().getPath(folderPath);

        try {
            Files.walkFileTree(folder, EnumSet.noneOf(FileVisitOption.class), Integer.MAX_VALUE, new PermissionVisitor());
        } catch (Exception e) {
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
