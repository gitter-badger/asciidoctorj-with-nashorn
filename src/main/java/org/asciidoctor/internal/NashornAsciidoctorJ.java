package org.asciidoctor.internal;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.script.SimpleBindings;
import javax.script.SimpleScriptContext;

import org.asciidoctor.ast.Document;
import org.asciidoctor.extension.ExtensionRegistry;

public class NashornAsciidoctorJ implements AsciidoctorJ {

    private static final String ASCIIDOCTOR_VERSION = "1.5.0";
    
    private static final String ASCIIDOCTOR_ALL_PATH = "META-INF/resources/webjars/asciidoctor.js/" + ASCIIDOCTOR_VERSION + "/asciidoctor-all.min.js";
    private static final String OPAL_PATH = "META-INF/resources/webjars/opal/0.6.2/opal.min.js";
    private static final String NASHORN_ENGINE = "nashorn";
    private ScriptEngine scriptEngine;
    private ScriptContext scriptContext;

    private NashornAsciidoctorJ(ScriptEngine scriptEngine, SimpleScriptContext scriptContext) {
        this.scriptEngine = scriptEngine;
        this.scriptContext = scriptContext;
    }

    public static AsciidoctorJ create() {
        ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
        return createNashornAsciidoctorJScriptEngineInstance(scriptEngineManager.getEngineByName(NASHORN_ENGINE));
    }

    public static AsciidoctorJ create(ClassLoader classLoader) {
        ScriptEngineManager scriptEngineManager = new ScriptEngineManager(classLoader);
        return createNashornAsciidoctorJScriptEngineInstance(scriptEngineManager.getEngineByName(NASHORN_ENGINE));
    }

    private static AsciidoctorJ createNashornAsciidoctorJScriptEngineInstance(ScriptEngine scriptEngine) {
        SimpleScriptContext simpleScriptContext = createScriptContext();
        loadResources(scriptEngine, simpleScriptContext);
        return new NashornAsciidoctorJ(scriptEngine, simpleScriptContext);
    }

    private static void loadResources(ScriptEngine scriptEngine, SimpleScriptContext scriptContext) {
        try {
            
            scriptEngine.eval(readScript(OPAL_PATH), scriptContext);
            scriptEngine.eval(readScript(ASCIIDOCTOR_ALL_PATH), scriptContext);
            scriptEngine.eval(new InputStreamReader(NashornAsciidoctorJ.class.getResourceAsStream("/asciidoctorjava.js")), scriptContext);
        } catch (ScriptException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private static String readScript(String path) {
        try (InputStream input = ClassLoader.getSystemResourceAsStream(path)) {
          return IOUtils.readFull(input);
        } catch (IOException e) {
          throw new RuntimeException("Unable to read " + path, e);
        }
      }
    
    private static SimpleScriptContext createScriptContext() {
        SimpleScriptContext simpleScriptContext = new SimpleScriptContext();
        Bindings bindings = new SimpleBindings();
        simpleScriptContext.setBindings(bindings, ScriptContext.ENGINE_SCOPE);
        return simpleScriptContext;
    }

    @Override
    public String convert(String source) {
        return this.convert(source, new HashMap<>());
    }

    @Override
    public String convert(String source, Map<String, Object> options) {
        
        Object renderedContent;
        
        try {
            Object hashOptions = createHash2Options(options);
            this.scriptContext.getBindings(ScriptContext.ENGINE_SCOPE).put("content", source);
            this.scriptContext.getBindings(ScriptContext.ENGINE_SCOPE).put("hash2", hashOptions);
            
            //We cannot use Invocable interface due a bug found in Nashorn. That bug will be fixed in Java8u40. For now eval approach will be used.
            renderedContent = (this.scriptEngine).eval("render(content, hash2);", this.scriptContext);
        } catch (ScriptException e) {
            throw new IllegalArgumentException(e);
        }
        
        return returnExpectedValue(renderedContent);
    }

    //TODO extract to NashornHashUtil class.
    private Object createHash2Options(Map<String, Object> options) throws ScriptException {
        this.scriptContext.getBindings(ScriptContext.ENGINE_SCOPE).put("listOptions", options.keySet());
        this.scriptContext.getBindings(ScriptContext.ENGINE_SCOPE).put("options", options);
        
        return this.scriptEngine.eval("Opal.hash2(listOptions, options)", this.scriptContext);
    }
    
    @Override
    public String convert_file(File filename) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String convert_file(File filename, Map<String, Object> options) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * This method has been added to deal with the fact that asciidoctor 0.1.2 can return an Asciidoctor::RubyDocument or a
     * String depending if content is write to disk or not. This may change in the future
     * (https://github.com/asciidoctor/asciidoctor/issues/286)
     * 
     * @param object
     * @return
     */
    private String returnExpectedValue(Object object) {
        if (object instanceof String) {
            return object.toString();
        } else {
            return null;
        }
    }
    
    @Override
    public Document load(String content, Map<String, Object> options) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Document load_file(File filename, Map<String, Object> options) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <T extends ExtensionRegistry> T createExtensionRegistry(Class<T> type) throws IllegalArgumentException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void unregisterAllExtensions() {
        // TODO Auto-generated method stub

    }

    @Override
    public void shutdown() {
        // TODO Auto-generated method stub

    }

    @Override
    public String asciidoctorVersion() {
        // TODO Auto-generated method stub
        return null;
    }

}