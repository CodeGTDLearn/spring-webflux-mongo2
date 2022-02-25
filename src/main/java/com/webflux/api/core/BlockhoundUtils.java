package com.webflux.api.core;

import lombok.NoArgsConstructor;
import reactor.blockhound.BlockHound;
import reactor.blockhound.integration.BlockHoundIntegration;

@NoArgsConstructor
public class BlockhoundUtils {

  //EXCECOES DE METODOS BLOQUEANTES DETECTADOS PELO BLOCKHOUND:
  static BlockHoundIntegration AllowedCalls =
       builder -> builder
            .allowBlockingCallsInside("java.io.PrintStream",
                                      "write"
                                     )
            .allowBlockingCallsInside("java.io.FileOutputStream",
                                      "writeBytes"
                                     )
            .allowBlockingCallsInside("java.io.BufferedOutputStream",
                                      "flushBuffer"
                                     )
            .allowBlockingCallsInside("java.io.BufferedOutputStream",
                                      "flush"
                                     )
            .allowBlockingCallsInside("java.io.OutputStreamWriter",
                                      "flushBuffer"
                                     )
            .allowBlockingCallsInside("java.io.PrintStream",
                                      "print"
                                     )
            //problems with transactions
            .allowBlockingCallsInside("java.util.UUID",
                                      "randomUUID"
                                     )
            .allowBlockingCallsInside("java.io.PrintStream",
                                      "println"
                                     );


  public static void blockhoundInstallAllowAllCalls() {

    BlockHound.install(AllowedCalls);
  }


  public static void blockhoundInstallSimple() {

    BlockHoundIntegration allowedCalls =
         builder -> builder
              .allowBlockingCallsInside("java.io.PrintStream",
                                        "write"
                                       )
              .allowBlockingCallsInside("java.util.concurrent.ConcurrentMap",
                                        "computeIfAbsent"
                                       );
    BlockHound.install(allowedCalls);
  }

}