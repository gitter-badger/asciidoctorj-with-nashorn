package org.asciidoctor.ruby.extension;

import org.asciidoctor.Options;
import org.asciidoctor.ast.*;
import org.asciidoctor.ruby.ast.RubyDocument;
import org.asciidoctor.ruby.internal.JRubyRuntimeContext;
import org.asciidoctor.ruby.internal.RubyHashUtil;
import org.asciidoctor.ruby.internal.RubyUtils;
import org.jruby.Ruby;
import org.jruby.RubyHash;
import org.jruby.javasupport.JavaEmbedUtils;
import org.jruby.runtime.builtin.IRubyObject;

import java.util.List;
import java.util.Map;

public class Processor {

	protected RubyHash config;
    protected Ruby rubyRuntime;

    public Processor(Map<String, Object> config) {
        this.rubyRuntime = JRubyRuntimeContext.get();
        this.config = RubyHashUtil.convertMapToRubyHashWithSymbols(rubyRuntime, config);
    }

    public void update_config(Map<String, Object> config) {
    	this.config.putAll(config);
    }
    
    public Map<Object, Object> getConfig() {
    	return this.config;
    }
    
    public Block createBlock(AbstractBlock parent, String context, String content, Map<String, Object> attributes,
            Map<Object, Object> options) {

        options.put(Options.SOURCE, content);
        options.put(Options.ATTRIBUTES, attributes);        
        
        return createBlock(parent, context, options);
    }
    
    public Block createBlock(AbstractBlock parent, String context, List<String> content, Map<String, Object> attributes,
            Map<Object, Object> options) {

        options.put(Options.SOURCE, content);
        options.put(Options.ATTRIBUTES, attributes);        
        
        return createBlock(parent, context, options);
    }

    public Inline createInline(AbstractBlock parent, String context, List<String> text, Map<String, Object> attributes, Map<Object, Object> options) {
        
        options.put(Options.ATTRIBUTES, attributes);
        
        IRubyObject rubyClass = rubyRuntime.evalScriptlet("Asciidoctor::Inline");
        RubyHash convertMapToRubyHashWithSymbols = RubyHashUtil.convertMapToRubyHashWithSymbolsIfNecessary(rubyRuntime,
                options);
        Object[] parameters = {
                parent.delegate(),
                RubyUtils.toSymbol(rubyRuntime, context),
                text,
                convertMapToRubyHashWithSymbols };
        return (Inline) JavaEmbedUtils.invokeMethod(rubyRuntime, rubyClass,
                "new", parameters, Inline.class);
    }
    
    public Inline createInline(AbstractBlock parent, String context, String text, Map<String, Object> attributes, Map<String, Object> options) {
        
        options.put(Options.ATTRIBUTES, attributes);
        
        IRubyObject rubyClass = rubyRuntime.evalScriptlet("Asciidoctor::Inline");
        RubyHash convertedOptions = RubyHashUtil.convertMapToRubyHashWithSymbols(rubyRuntime, options);
        // FIXME hack to ensure we have the underlying Ruby instance
        try {
            parent = parent.delegate();
        } catch (Exception e) {}

        Object[] parameters = {
                parent,
                RubyUtils.toSymbol(rubyRuntime, context),
                text,
                convertedOptions };
        return (Inline) JavaEmbedUtils.invokeMethod(rubyRuntime, rubyClass,
                "new", parameters, Inline.class);
    }
    
    protected RubyDocument rubyDocument(Document document) {
    	return new RubyDocument(document, rubyRuntime);
    }
    
    private Block createBlock(AbstractBlock parent, String context,
            Map<Object, Object> options) {
        IRubyObject rubyClass = rubyRuntime.evalScriptlet("Asciidoctor::Block");
        RubyHash convertMapToRubyHashWithSymbols = RubyHashUtil.convertMapToRubyHashWithSymbolsIfNecessary(rubyRuntime,
                options);
        // FIXME hack to ensure we have the underlying Ruby instance
        try {
            parent = parent.delegate();
        } catch (Exception e) {}

        Object[] parameters = {
                parent,
                RubyUtils.toSymbol(rubyRuntime, context),
                convertMapToRubyHashWithSymbols };
        return (Block) JavaEmbedUtils.invokeMethod(rubyRuntime, rubyClass,
                "new", parameters, Block.class);
    }

}
