#Polling File Reader to kick off Spring Integration Process

As files are received they will be processed, File name is used as the Job Parameter, so the same file cannot be processed twice.


##Item Reader

Reads Json Elements and maps to HashMap

##Item Processor

Does Nothing,  passes onto ItemWriter

##ItemWriter
Calls the Tamr API via REST. Records will be put in batch based on the configured chunk size.


###JobRequestTransformer
Spring Integration event to convert an incoming file to a new Job Request.