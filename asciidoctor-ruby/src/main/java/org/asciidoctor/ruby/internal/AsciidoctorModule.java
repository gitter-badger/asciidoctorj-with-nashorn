package org.asciidoctor.ruby.internal;

import java.util.Map;

import org.asciidoctor.ast.Document;
import org.asciidoctor.ruby.extension.BlockMacroProcessor;
import org.asciidoctor.ruby.extension.BlockProcessor;
import org.asciidoctor.ruby.extension.IncludeProcessor;
import org.asciidoctor.ruby.extension.InlineMacroProcessor;
import org.asciidoctor.ruby.extension.Postprocessor;
import org.asciidoctor.ruby.extension.Preprocessor;
import org.asciidoctor.ruby.extension.Treeprocessor;
import org.jruby.RubyClass;


public interface AsciidoctorModule {

    void preprocessor(String preprocessorClassName);
    void preprocessor(RubyClass preprocessorClassName);
	void preprocessor(Preprocessor preprocessor);
	
    void postprocessor(String postprocessorClassName);
    void postprocessor(RubyClass postprocessorClassName);
    void postprocessor(Postprocessor postprocessor);
    
    void treeprocessor(String treeprocessor);
    void treeprocessor(RubyClass treeprocessorClassName);
    void treeprocessor(Treeprocessor treeprocessorClassName);
    
    void include_processor(String includeProcessorClassName);
    void include_processor(RubyClass includeProcessorClassName);
    void include_processor(IncludeProcessor includeProcessor);
    
    void block_processor(String blockClassName, Object blockName);
    void block_processor(RubyClass blockClass, Object blockName);
    void block_processor(BlockProcessor blockInstance, Object blockName);
    
    void block_macro(String blockMacroClassName, Object blockName);
    void block_macro(Class<BlockMacroProcessor> blockMacroClass, Object blockName);
    void block_macro(BlockMacroProcessor blockMacroInstance, Object blockName);
    
    void inline_macro(String blockClassName, Object blockSymbol);
    void inline_macro(RubyClass blockClassName, Object blockSymbol);
    void inline_macro(InlineMacroProcessor blockClassName, Object blockSymbol);
    
    
    
    void unregister_all_extensions();
    
	Object convert(String content, Map<String, Object> options);
	Object convertFile(String filename, Map<String, Object> options);
	
	Document load_file(String filename, Map<String, Object> options);
	Document load(String content, Map<String, Object> options);

	String asciidoctorRuntimeEnvironmentVersion();
	
}
