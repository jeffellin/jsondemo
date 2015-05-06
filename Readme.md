#Polling File Reader to kick off Spring Integration Process

As files are received they will be processed, 

##Item Reader

Reads Json Elements and maps to HashMap

##Item Processor

Does Nothing,  passes onto ItemWriter

##ItemWriter
Calls the Tamr API via REST. Records will be put in batch based on the configured chunk size.
