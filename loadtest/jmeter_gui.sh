
# Purpose
#  - start up the JMeter gui 
#  - uses the exact JMeter run-time and dependencies that will be used during execution of a test driven my jmeter-maven-plugin

java -Xms512M -Xmx512M -jar target/jmeter/bin/ApacheJMeter.jar -l target/jmeter/results/20140411-ref_arch.jtl -d target/jmeter -j target/jmeter/logs/ref_arch.jmx.log -t src/test/jmeter/ref_arch.jmx
