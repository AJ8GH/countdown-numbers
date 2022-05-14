package io.github.aj8gh.countdown.out.file;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class FileProvider {
    String ioDir;
    String genInFile;
    String genOutFile;
    String solInFile;
    String solOutFile;
}
