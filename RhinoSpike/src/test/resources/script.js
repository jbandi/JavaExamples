print("Hello from Script!")


var impl = new Object();
impl.run = function() {print("Yeah that's right, you better run!");};
var runnable = new java.lang.Runnable(impl);
var thread = new java.lang.Thread(runnable);
thread.run();
