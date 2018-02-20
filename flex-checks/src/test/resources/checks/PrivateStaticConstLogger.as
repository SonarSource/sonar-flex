class A {

  public const logger:ILogger = LogUtil.getLogger(A);           // Noncompliant {{Make the logger "logger" private static const and rename it to comply with the format "LOG(?:GER)?".}}
  public const LOGGER:ILogger = LogUtil.getLogger(A);           // Noncompliant {{Make the logger "LOGGER" private static const.}}
  private static const fancyLog:ILogger = LogUtil.getLogger(A); // Noncompliant {{Rename the "fancyLog" logger to comply with the format "LOG(?:GER)?".}}


  public const LOG:ILogger = LogUtil.getLogger(A);              // Noncompliant
  private const LOG:ILogger = LogUtil.getLogger(A);             // Noncompliant
  public static var LOG:ILogger = LogUtil.getLogger(A);         // Noncompliant
  public static var LOG:ILogger = LogUtil.getLogger(A);         // Noncompliant


  private static const LOG:ILogger = LogUtil.getLogger(A);      // OK
  private static const LOGGER:ILogger = LogUtil.getLogger(A);   // OK

  private static const logger:Logger = LogUtil.getLogger(A);    // OK
  private static const logger = LogUtil.getLogger(A);           // OK

  public function f(){}
}
