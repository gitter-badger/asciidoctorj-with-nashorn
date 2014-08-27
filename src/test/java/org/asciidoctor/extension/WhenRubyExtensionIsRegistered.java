package org.asciidoctor.extension;

import static org.asciidoctor.OptionsBuilder.options;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.File;

import org.asciidoctor.Asciidoctor;
import org.asciidoctor.internal.JRubyAsciidoctorOld;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Test;

public class WhenRubyExtensionIsRegistered {

    private Asciidoctor asciidoctor = JRubyAsciidoctorOld.create();

    @Test
    public void ruby_extension_should_be_registered() {
        
        RubyExtensionRegistry rubyExtensionRegistry = this.asciidoctor.rubyExtensionRegistry();
        rubyExtensionRegistry.loadClass(Class.class.getResourceAsStream("/YellRubyBlock.rb")).block("rubyyell", "YellRubyBlock");

        String content = asciidoctor.renderFile(new File(
                "target/test-classes/sample-with-ruby-yell-block.ad"),
                options().toFile(false).get());
        Document doc = Jsoup.parse(content, "UTF-8");
        Elements elements = doc.getElementsByClass("paragraph");
        assertThat(elements.size(), is(2));
        assertThat(elements.get(1).text(), is("THE TIME IS NOW! GET A MOVE ON!"));
        
    }

}
