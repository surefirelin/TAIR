/**
 * Copyright (c) 2014, the TEE2 AUTHORS.
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * Neither the name of the University of Bari nor the names of its contributors
 * may be used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * GNU GENERAL PUBLIC LICENSE - Version 3, 29 June 2007
 *
 */
package di.uniba.it.tee2.wiki;

import de.tudarmstadt.ukp.wikipedia.parser.ParsedPage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPOutputStream;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 *
 * @author pierpaolo
 */
public class Wikidump2Text {

    private static String encoding = "UTF-8";

    private static final String notValidTitle = "^[A-Za-z\\s_-]+:[A-Za-z].*$";

    static final Options options;

    static CommandLineParser cmdParser = new BasicParser();

    static {
        options = new Options();
        options.addOption("l", true, "language (italian, english)")
                .addOption("d", true, "the Wikipedia dump")
                .addOption("o", true, "the output file")
                .addOption("e", true, "charset encoding (optional)");
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            CommandLine cmd = cmdParser.parse(options, args);
            if (cmd.hasOption("l") && cmd.hasOption("d") && cmd.hasOption("o")) {
                encoding = cmd.getOptionValue("e", "UTF-8");
                int counter = 0;
                try {
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new GZIPOutputStream(new FileOutputStream(cmd.getOptionValue("o"))), "UTF-8"));
                    WikipediaDumpIterator it = new WikipediaDumpIterator(new File(cmd.getOptionValue("d")), encoding);
                    PageCleaner cleaner = PageCleanerWrapper.getInstance(cmd.getOptionValue("l"));
                    while (it.hasNext()) {
                        WikiPage wikiPage = it.next();
                        ParsedPage parsedPage = wikiPage.getParsedPage();
                        if (parsedPage != null) {
                            String title = wikiPage.getTitle();
                            if (!title.matches(notValidTitle)) {
                                if (parsedPage.getText() != null) {
                                    writer.append(cleaner.clean(parsedPage.getText()));
                                    writer.newLine();
                                    writer.newLine();
                                    counter++;
                                    if (counter % 10000 == 0) {
                                        System.out.println(counter);
                                        writer.flush();
                                    }
                                }
                            }
                        }
                    }
                    writer.flush();
                    writer.close();
                } catch (Exception ex) {
                    Logger.getLogger(Wikidump2Text.class.getName()).log(Level.SEVERE, null, ex);
                }
                System.out.println("Indexed pages: " + counter);
            } else {
                HelpFormatter helpFormatter = new HelpFormatter();
                helpFormatter.printHelp("Wikipedia dump to text", options, true);
            }
        } catch (ParseException ex) {
            Logger.getLogger(Wikidump2Text.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
