package jsondemo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.fasterxml.jackson.datatype.jdk7.Jdk7Module;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.google.common.io.ByteSource;
import jsondemo.service.UpdateService;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

/**
 * Created by jellin on 5/6/15.
 */
public class DummyWriter implements ItemWriter<Map<String,String>> {

    @Autowired
    UpdateService service;

    @Override
    public void write(List<? extends Map<String, String>> list) throws Exception {

        ByteSource bs = createTempQuerySource((List<Map<String, String>>) list);
        service.dosomething(bs);
;
    }


  public static ObjectMapper generateDefaultObjectMapper() {
        return new ObjectMapper().registerModules(
                new GuavaModule(),
                new Jdk7Module(),
                new Jdk8Module()
        );
    }


    private ByteSource createTempQuerySource(final List<Map<String, String>> queryRecords) {
        final ObjectMapper mapper = generateDefaultObjectMapper();
        final Path tmp;
        try {
            tmp = Files.createTempFile("Tamr", "json");
            try (final BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(new FileOutputStream(tmp.toFile())))) {
                for (final Map<String, String> query : queryRecords) {
                    writer.write(mapper.writeValueAsString(query));
                }
            }
        } catch (IOException ioe) {
            throw new UncheckedIOException(ioe);
        }
        return com.google.common.io.Files.asByteSource(tmp.toFile());
    }


}
