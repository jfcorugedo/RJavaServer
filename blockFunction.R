library(blockTools)
library(plyr)

# Block function for continuous variables
blockFunction <- function(data,id.vars,block.vars) {
  
  # Check if the variance of all block.vars is zero. The block function doesn't work in that case
  allVarsAreZero = TRUE
  for(blockVar in block.vars) {
    if(var(data[[blockVar]]) != 0) allVarsAreZero = FALSE
  }
  
  # If the variance of all block.vars is not zero it's safe to call the block function
  if(!allVarsAreZero) {
    result <- block(data, 
                    n.tr = 2, # Match pairs, if 3 match triplets, and so on. 
                    id.vars = id.vars,  
                    block.vars = block.vars,    
                    algorithm="optGreedy",  # Ok default
                    distance = "mahalanobis",  # OK default
                    level.two = FALSE, 
                    verbose = FALSE)
    
    return(result$blocks[[1]]) 
  }
  # The variance of all block.vars is zero. That means that all participants have the same value for
  # the block vars. We return a combination of pair randomly choosen among the original data
  else {
    result <- replicate(nrow(data)/2,sample(data[[id.vars]],2))   # Generate pairs
    result <- adply(result,2,.id=NULL)                            # Convert array to data.frame
    colnames(result) <- c("Unit 1","Unit 2")                      # Change column names to "Unit 1", "Unit2"
    
    return(result)
  }
  
  
}

# Adds a column to the data.frame with the name value. The value of that column will be 1 if the row has
# that value for the column var and 0 otherwise. For example, if var = city and value = Madrid a new column
# Madrid will be added with 1 if the row has city == Madrid or 0 otherwise. 
addColumnForVariable <- function(data,var,value) {
  
  data[data[[var]] %in% value,value] <- 1
  data[!(data[[var]] %in% value),value] <- 0
  
  
  return(data)
  
}

# Block function for discrete variables. It creates artificial variables for every value of the
# discrete variables and use them as block variables in the block function
blockDiscreteFunction <- function(data,id.vars,block.vars) {
   
  newBlockVars = NULL
  
  # We might have mutiple block.vars
  for(var in block.vars) {
    uniqueValues = as.character(unique(data[[var]]))        # Get unique values for the blocking variable
    uniqueValues = uniqueValues[1:length(uniqueValues)-1]   # Get all values except the last one. Block function doesn't work with all values

    newBlockVars = append(uniqueValues,newBlockVars)        # Add the values to the blockVars vector
    
    for(value in uniqueValues) {                            # For every value, create a column with that value
      data <- addColumnForVariable(data,var,value)
    }
  }
  
  result <- blockFunction(data,id.vars,newBlockVars)        #Do the matching using the new columns
  
  return(result)
}

