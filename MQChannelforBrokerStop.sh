##########################################################
######## UNIX SCRIPT TO STOP CHANNELS #########
######## AUTHOR: ABHIJIT KARMAKAR #######################
######## VERSION: 1.0 DATE: 1-Sep-2014 ##################
##########################################################

chlname=`echo $1`
QMNAME=`echo $2`
temp1=`echo $2 | cut -c1-11`
temp2=`echo $2 | cut -c15`
broker=$temp1$temp2
cmdstatus="OK"
echo "The Channel from the broker ${broker} to ATG is going to be stopped..."
echo "The Channel ${chlname} of the Queue manager ${QMNAME} is going to be stopped..."
result=`echo "stop channel(${chlname})" | runmqsc $QMNAME`
echo "$result"
status=$?	
if [ $status -eq 0 ];then
   echo "Command to stop the Channel ${chlname} issued successfully" 
   if echo "$result" | (grep "AMQ9514") then
		echo "Status OK."
		exit 0
   elif echo "$result" | (grep "AMQ9533") then
		echo "Status OK."
		exit 0		
   elif echo "$result" | (grep "AMQ9509") then
		echo "Status FAIL."
		exit 1
   elif echo "$result" | (grep "AMQ8227") then
		echo "Status FAIL."
		exit 1
   elif echo "$result" | (grep "AMQ8118") then
		echo "Status FAIL."
		exit 1
   elif echo "$result" | (grep "AMQ8146") then
		echo "Status FAIL."
		exit 1			
   else
		echo "Status OK."
		exit 0
   fi
else
   echo "Command to stop the Channel ${chlname} processed with warning needs to be investigated by support" 
   exit 1
fi