package io.ngocnhan_tran1996.code.example.trace;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PACKAGE)
final class SourceFileVisitor extends SimpleFileVisitor<Path> {

    final Map<String, Set<Path>> pathsByName = new HashMap<>();

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {

        String fileName = file.getFileName()
            .toString();
        pathsByName.computeIfAbsent(fileName, k -> new HashSet<>())
            .add(file);

        return super.visitFile(file, attrs);
    }

}