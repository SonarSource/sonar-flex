class A {

  public const logger:ILogger = LogUtil.getLogger(A);           // NOK
  public const LOGGER:ILogger = LogUtil.getLogger(A);
  private static const fancyLog:ILogger = LogUtil.getLogger(A); // NOK


  public const LOG:ILogger = LogUtil.getLogger(A);              // NOK
  private const LOG:ILogger = LogUtil.getLogger(A);             // NOK
  public static var LOG:ILogger = LogUtil.getLogger(A);         // NOK
  public static var LOG:ILogger = LogUtil.getLogger(A);         // NOK


  private static const LOG:ILogger = LogUtil.getLogger(A);      // OK
  private static const LOGGER:ILogger = LogUtil.getLogger(A);   // OK

  private static const logger:Logger = LogUtil.getLogger(A);    // OK
  private static const logger = LogUtil.getLogger(A);           // OK

  public function f(){}
}
