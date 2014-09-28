package org.asciidoctor.ruby.extension;

import static org.asciidoctor.OptionsBuilder.options;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.IOException;

import org.asciidoctor.Asciidoctor;
import org.asciidoctor.Options;
import org.asciidoctor.SafeMode;
import org.asciidoctor.ruby.internal.RubyAsciidoctorJ;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class WhenExtensionIsRegisteredAsService {

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    @Ignore("Test is ignored because currently it is not possible to register two block extensions in same instance. This may require deep changes on Asciidoctor Extensions API")
    @Test
    public void extensions_should_be_correctly_added() throws IOException {

        
        Asciidoctor asciidoctor = new Asciidoctor(RubyAsciidoctorJ.Factory.create());
        
        //To avoid registering the same extension over and over for all tests, service is instantiated manually.
        new ArrowsAndBoxesExtension().register(asciidoctor.getAsciidoctorJ());
        
        Options options = options().inPlace(false)
                .toFile(new File(testFolder.getRoot(), "rendersample.html"))
                .safe(SafeMode.UNSAFE).get();

        asciidoctor.convertFile(new File(
                "target/test-classes/arrows-and-boxes-example.ad"), options);

        File renderedFile = new File(testFolder.getRoot(), "rendersample.html");
        Document doc = Jsoup.parse(renderedFile, "UTF-8");
        
        Element arrowsJs = doc.select("script[src=http://www.headjump.de/javascripts/arrowsandboxes.js").first();
        assertThat(arrowsJs, is(notNullValue()));
        
        Element arrowsCss = doc.select("link[href=http://www.headjump.de/stylesheets/arrowsandboxes.css").first();
        assertThat(arrowsCss, is(notNullValue()));
        
        Element arrowsAndBoxes = doc.select("pre[class=arrows-and-boxes").first();
        assertThat(arrowsAndBoxes, is(notNullValue()));
        
    }

}
