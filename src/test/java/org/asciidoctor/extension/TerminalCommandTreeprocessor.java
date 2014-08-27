package org.asciidoctor.extension;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.asciidoctor.ast.AbstractBlock;
import org.asciidoctor.ast.Block;
import org.asciidoctor.ast.RubyDocument;

public class TerminalCommandTreeprocessor extends Treeprocessor {

	private RubyDocument rubyDocument;
	
    public TerminalCommandTreeprocessor(Map<String, Object> config) {
        super(config);
    }

    @Override
    public RubyDocument process(RubyDocument rubyDocument) {

    	this.rubyDocument = rubyDocument;
    	
        final List<AbstractBlock> blocks = this.rubyDocument.blocks();

        for (int i = 0; i < blocks.size(); i++) {
            final AbstractBlock currentBlock = blocks.get(i);
            if(currentBlock instanceof Block) {
                Block block = (Block)currentBlock;
                List<String> lines = block.lines();
                if (lines.size() > 0 && lines.get(0).startsWith("$")) {
                    blocks.set(
                            i, convertToTerminalListing(block));
                            
                }
            }
        }
        
        return this.rubyDocument;
    }

    public Block convertToTerminalListing(Block block) {

        Map<String, Object> attributes = block.attributes();
        attributes.put("role", "terminal");
        StringBuilder resultLines = new StringBuilder();

        List<String> lines = block.lines();

        for (String line : lines) {
            if (line.startsWith("$")) {
                resultLines.append("<span class=\"command\">")
                        .append(line.substring(2, line.length()))
                        .append("</command");
            } else {
                resultLines.append(line);
            }
        }

        return createBlock(this.rubyDocument, "listing", Arrays.asList(resultLines.toString()), attributes,
                new HashMap<Object, Object>());
    }

}
