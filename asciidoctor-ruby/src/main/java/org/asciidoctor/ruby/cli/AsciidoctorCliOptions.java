package org.asciidoctor.ruby.cli;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.asciidoctor.AttributesBuilder;
import org.asciidoctor.Options;
import org.asciidoctor.OptionsBuilder;
import org.asciidoctor.SafeMode;

import com.beust.jcommander.Parameter;

public class AsciidoctorCliOptions {

    public static final String LOAD_PATHS = "-I";
    public static final String REQUIRE = "-r";
    public static final String QUIET = "-q";
    public static final String ATTRIBUTE = "-a";
    public static final String HELP = "-h";
    public static final String DESTINATION_DIR = "-D";
    public static final String BASE_DIR = "-B";
    public static final String TEMPLATE_DIR = "-T";
    public static final String TEMPLATE_ENGINE = "-E";
    public static final String COMPACT = "-C";
    public static final String ERUBY = "-e";
    public static final String SECTION_NUMBERS = "-n";
    public static final String NO_HEADER_FOOTER = "-s";
    public static final String SAFE = "-S";
    public static final String OUTFILE = "-o";
    public static final String DOCTYPE = "-d";
    public static final String BACKEND = "-b";
    public static final String VERSION = "-V";
    public static final String VERBOSE = "-v";
    public static final char ATTRIBUTE_SEPARATOR = '=';
    public static final String MONITOR_OPTION_NAME = "monitor";

    @Parameter(names = { VERBOSE, "--verbose" }, description = "enable verbose mode (default: false)")
    private boolean verbose = false;

    @Parameter(names = { VERSION, "--version" }, description = "display the version and runtime environment")
    private boolean version = false;

    @Parameter(names = { BACKEND, "--backend" }, description = "set output format backend (default: html5)")
    private String backend = "html5";

    @Parameter(names = { DOCTYPE, "--doctype" }, description = "document type to use when rendering output: [article, book, inline] (default: article)")
    private String doctype = "article";

    @Parameter(names = { OUTFILE, "--out-file" }, description = "output file (default: based on input file path); use - to output to STDOUT")
    private String outFile;

    @Parameter(names = { "--safe" }, description = "set safe mode level to safe (default: unsafe)")
    private boolean safe = false;

    @Parameter(names = { SAFE, "--safe-mode" }, converter = SafeModeConverter.class, description = "set safe mode level explicitly: [unsafe, safe, server, secure] (default: unsafe)")
    private SafeMode safeMode = SafeMode.UNSAFE;

    @Parameter(names = { NO_HEADER_FOOTER, "--no-header-footer" }, description = "suppress output of header and footer (default: false)")
    private boolean noHeaderFooter = false;

    @Parameter(names = { SECTION_NUMBERS, "--section-numbers" }, description = "auto-number section titles in the HTML backend; disabled by default")
    private boolean sectionNumbers = false;

    @Parameter(names = { ERUBY, "--eruby" }, description = "specify eRuby implementation to render built-in templates: [erb, erubis] (default: erb)")
    private String eruby = "erb";

    @Parameter(names = { COMPACT, "--compact" }, description = "compact the output by removing blank lines (default: false)")
    private boolean compact = false;

    @Parameter(names = { TEMPLATE_ENGINE, "--template-engine" }, description = "template engine to use for the custom render templates (loads gem on demand)")
    private String templateEngine;

    @Parameter(names = { TEMPLATE_DIR, "--template-dir" }, description = "directory containing custom render templates the override the built-in set")
    private List<String> templateDir;

    @Parameter(names = { BASE_DIR, "--base-dir" }, description = "base directory containing the document and resources (default: directory of source file)")
    private String baseDir;

    @Parameter(names = { DESTINATION_DIR, "--destination-dir" }, description = "destination output directory (default: directory of source file)")
    private String destinationDir;

    @Parameter(names = { "--trace" }, description = "include backtrace information on errors (default: false)")
    private boolean trace = false;

    @Parameter(names = { HELP, "--help" }, help = true, description = "show this message")
    private boolean help = false;

    @Parameter(names = { ATTRIBUTE, "--attribute" }, description = "a list of attributes, in the form key or key=value pair, to set on the document")
    private List<String> attributes = new ArrayList<String>();

    @Parameter(names = { QUIET, "--quiet" }, description = "suppress warnings (default: false)")
    private boolean quiet = false;

    @Parameter(names = { REQUIRE, "--require" }, description = "require the specified library before executing the processor (using require)")
    private List<String> require;

    @Parameter(names = { LOAD_PATHS, "--load-path" }, description = "add a directory to the $LOAD_PATH may be specified more than once", variableArity = true)
    private List<String> loadPaths = new ArrayList<String>();

    @Parameter(description = "input files")
    private List<String> parameters = new ArrayList<String>();

    public boolean isQuiet() {
        return quiet;
    }

    public boolean isRequire() {
        return this.require != null && this.require.size() > 0;
    }

    public List<String> getRequire() {
        return require;
    }

    public boolean isLoadPaths() {
        return this.loadPaths != null && this.loadPaths.size() > 0;
    }

    public List<String> getLoadPaths() {
        return this.loadPaths;
    }

    public List<String> getParameters() {
        return this.parameters;
    }

    public boolean isVerbose() {
        return this.verbose;
    }

    public String getBackend() {
        return this.backend;
    }

    public String getDoctype() {
        return this.doctype;
    }

    public String getOutFile() {
        return this.outFile;
    }

    public boolean isOutFileOption() {
        return this.outFile != null;
    }

    public boolean isSafe() {
        return this.safe;
    }

    public SafeMode getSafeMode() {
        return this.safeMode;
    }

    public boolean isNoHeaderFooter() {
        return this.noHeaderFooter;
    }

    public boolean isSectionNumbers() {
        return this.sectionNumbers;
    }

    public String getEruby() {
        return this.eruby;
    }

    public boolean isCompact() {
        return this.compact;
    }

    public List<String> getTemplateDir() {
        return this.templateDir;
    }

    public boolean isTemplateDirOption() {
        return this.templateDir != null;
    }

    public String getBaseDir() {
        return this.baseDir;
    }

    public boolean isBaseDirOption() {
        return this.baseDir != null;
    }

    public String getDestinationDir() {
        return this.destinationDir;
    }

    public boolean isDestinationDirOption() {
        return this.destinationDir != null;
    }

    public boolean isTemplateEngineOption() {
        return this.templateEngine != null;
    }

    public boolean isTrace() {
        return this.trace;
    }

    public boolean isHelp() {
        return this.help;
    }

    public boolean isVersion() {
        return this.version;
    }

    private boolean isOutputStdout() {
        return "-".equals(getOutFile());
    }

    private boolean isInPlaceRequired() {
        return !isOutFileOption() && !isDestinationDirOption() && !isOutputStdout();
    }

    public Options parse() {
        OptionsBuilder optionsBuilder = OptionsBuilder.options();
        AttributesBuilder attributesBuilder = AttributesBuilder.attributes();

        optionsBuilder.backend(this.backend).safe(this.safeMode).docType(this.doctype).eruby(this.eruby);

        if (isOutFileOption() && !isOutputStdout()) {
            optionsBuilder.toFile(new File(this.outFile));
        }

        if (isOutFileOption() && isOutputStdout()) {
            optionsBuilder.toFile(false);
        }

        if (this.safe) {
            optionsBuilder.safe(SafeMode.SAFE);
        }

        if (this.noHeaderFooter) {
            optionsBuilder.headerFooter(false);
        }

        if (this.sectionNumbers) {
            attributesBuilder.sectionNumbers(this.sectionNumbers);
        }

        if (this.compact) {
            optionsBuilder.compact(this.compact);
        }

        if (isBaseDirOption()) {
            optionsBuilder.baseDir(new File(this.baseDir));
        }

        if (isTemplateEngineOption()) {
            optionsBuilder.templateEngine(this.templateEngine);
        }

        if (isTemplateDirOption()) {
            for (String templateDir : this.templateDir) {
                optionsBuilder.templateDir(new File(templateDir));
            }
        }

        if (isDestinationDirOption() && !isOutputStdout()) {
            optionsBuilder.toDir(new File(this.destinationDir));
        }

        if (isInPlaceRequired()) {
            optionsBuilder.inPlace(true);
        }

        if (this.verbose) {
            optionsBuilder.option(MONITOR_OPTION_NAME, new HashMap<Object, Object>());
        }

        attributesBuilder.attributes(getAttributes());
        optionsBuilder.attributes(attributesBuilder.get());
        return optionsBuilder.get();

    }

    public Map<String, Object> getAttributes() {

        Map<String, Object> attributeValues = new HashMap<String, Object>();

        for (String attribute : this.attributes) {
            int equalsIndex = -1;
            if ((equalsIndex = attribute.indexOf(ATTRIBUTE_SEPARATOR)) > -1) {
                extractAttributeNameAndValue(attributeValues, attribute, equalsIndex);
            } else {
                attributeValues.put(attribute, "");
            }
        }

        return attributeValues;
    }

    private void extractAttributeNameAndValue(Map<String, Object> attributeValues, String attribute, int equalsIndex) {
        String attributeName = attribute.substring(0, equalsIndex);
        String attributeValue = attribute.substring(equalsIndex + 1, attribute.length());

        attributeValues.put(attributeName, attributeValue);
    }
}
