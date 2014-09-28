package org.asciidoctor.ruby;

import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.hamcrest.CoreMatchers.is;

import java.io.File;
import java.util.List;

import org.asciidoctor.AsciiDocDirectoryWalker;
import org.asciidoctor.DirectoryWalker;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class WhenDirectoriesWithAsciidocFilesAreScanned {

	@ClassRule
	public static TemporaryFolder temporaryFolder = new TemporaryFolder();
	
	@Test
	public void only_asciidoc_files_should_be_returned() {

		DirectoryWalker abstractDirectoryWalker = new AsciiDocDirectoryWalker("target/test-classes/src/documents");
		List<File> asciidocFiles = abstractDirectoryWalker.scan();

		assertThat(
				asciidocFiles,
				containsInAnyOrder(new File("target/test-classes/src/documents/sample.ad"), new File(
						"target/test-classes/src/documents/sample.adoc"), new File(
						"target/test-classes/src/documents/sample.asciidoc"), new File(
						"target/test-classes/src/documents/sample.asc")));

	}

	@Test
	public void empty_directory_should_return_no_documents() {
		
		DirectoryWalker abstractDirectoryWalker = new AsciiDocDirectoryWalker(temporaryFolder.getRoot().getAbsolutePath());
		List<File> asciidocFiles = abstractDirectoryWalker.scan();
		
		assertThat(asciidocFiles, is(empty()));
		
	}
	
	@Test
	public void none_existing_directories_should_return_no_documents() {
		
		DirectoryWalker abstractDirectoryWalker = new AsciiDocDirectoryWalker("my_udirectory");
		List<File> asciidocFiles = abstractDirectoryWalker.scan();
		
		assertThat(asciidocFiles, is(empty()));
		
	}
	
}
