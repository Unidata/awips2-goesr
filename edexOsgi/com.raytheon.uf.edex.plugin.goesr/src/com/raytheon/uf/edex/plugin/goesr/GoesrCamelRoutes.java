/**
 * This software was developed and / or modified by Raytheon Company,
 * pursuant to Contract DG133W-05-CQ-1067 with the US Government.
 *
 * U.S. EXPORT CONTROLLED TECHNICAL DATA
 * This software product contains export-restricted data whose
 * export/transfer/disclosure is restricted by U.S. law. Dissemination
 * to non-U.S. persons whether in the United States or abroad requires
 * an export license or other authorization.
 *
 * Contractor Name:        Raytheon Company
 * Contractor Address:     6825 Pine Street, Suite 340
 *                         Mail Stop B8
 *                         Omaha, NE 68106
 *                         402.291.0100
 *
 * See the AWIPS II Master Rights File ("Master Rights File.pdf") for
 * further licensing information.
 **/

package com.raytheon.uf.edex.plugin.goesr;

import com.raytheon.uf.edex.routes.EDEXRouteBuilder;

/**
 * Camel routes converted from file "goesr-ingest.xml", context "goesr-camel"
 *
 * <pre>
 *
 * SOFTWARE HISTORY
 *
 * Date         Ticket#    Engineer    Description
 * ------------ ---------- ----------- --------------------------
 * 2024-08-23   2037701    aford       Initial creation (from auto-generated)
 *
 * </pre>
 */

//@formatter:off
/* Original XML definition:

    <camelContext id="goesr-camel" xmlns="http://camel.apache.org/schema/spring"
        errorHandlerRef="errorHandler">

       <endpoint id="goesrJmsEndpoint" uri="jms-durable:queue:Ingest.GOESR?concurrentConsumers=${goesr-decode.threads}" />
       <endpoint id="goesrDirectEndpoint" uri="direct-vm:goesr-decode" />

       <route id="goesrIngestRoute">
         <from uri="goesrJmsEndpoint" />
         <doTry>
           <to uri="goesrDirectEndpoint" />
           <doCatch>
             <exception>java.lang.Throwable</exception>
             <to uri="log:goesr?level=ERROR" />
           </doCatch>
         </doTry>
       </route>

       <route id="goesrIngestDecode">
         <from uri="goesrDirectEndpoint" />
         <setHeader name="pluginName">
           <constant>satellite</constant>
         </setHeader>
         <setHeader name="dataType">
           <constant>goes-r</constant>
         </setHeader>
         <pipeline>
            <bean ref="stringToFile" />
            <bean ref="getFileWithoutWmoHeader" />
            <split streaming="true" aggregationStrategy="notificationCountStrategy">
              <method ref="goesrDecoder" method="split"/>
              <doTry>
                  <pipeline>
                      <bean ref="goesrDecoder" method="decode" />
                      <to uri="direct-vm:persistIndexAlert" />
                  </pipeline>
                  <doCatch>
                      <exception>java.lang.Throwable</exception>
                      <to uri="log:goesr?level=ERROR" />
                  </doCatch>
              </doTry>
            </split>
         </pipeline>
       </route>
    </camelContext>
*/
//@formatter:on
public class GoesrCamelRoutes extends EDEXRouteBuilder {

    private final String goesrDecodeThreads;

    public GoesrCamelRoutes(String goesrDecodeThreads) {
        this.goesrDecodeThreads = goesrDecodeThreads;
    }

    @Override
    public void configure() throws Exception {
        // @formatter:off
        from("jms-durable:queue:Ingest.GOESR?concurrentConsumers=" + this.goesrDecodeThreads)
            .doTry()
                .to("direct:goesr-decode")
            .doCatch(Throwable.class)
                .to("log:goesr?level=ERROR")
            .endDoTry()
            .end()
            .setId("goesrIngestRoute");

        from("direct:goesr-decode")
            .setHeader("pluginName", constant("satellite"))
            .setHeader("dataType", constant("goes-r"))
            .pipeline()
                .bean("stringToFile")
                .bean("getFileWithoutWmoHeader")
                .split(method("goesrDecoder", "split")).streaming()
                    .doTry()
                        .pipeline()
                            .bean("goesrDecoder", "decode")
                            .to("direct:persistIndexAlert")
                    .endDoTry()
                    .doCatch(Throwable.class)
                        .to("log:goesr?level=ERROR")
                    .endDoTry()
                    .end()
                .end()
            .end()
            .setId("goesrIngestDecode");
        // @formatter:on
    }
}
