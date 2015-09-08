library(blockTools)

blockFunction <- function(data,id.vars,block.vars) {
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

