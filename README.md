# Learning from Observation Data Set Repository
This repository contains a collection of domains for evaluating machine learning from observation (LfO), and learning from demonstration (LfD) algorithms, also referred to in the literature as imitation learning or behavioral cloning.

Specifically, LfO is the task an agent faces when trying to learn to perform a task by observing another agent (the "demonstrator" or "expert").

Compared to data set repositories such as the UCI machine learning repository, LfO domains are more complex, since they involve an agent performing a task in some domain. Therefore, in addition to training data, each domain needs to provide a "simulator" to test learned agents on. Additionally, evaluation of the performance of agents is also more complex, since in some domains, assessing whether the task is performed might not be enough and we might want to evaluate how "similar to the demonstrator" a learning agent behaves.

For that reason, comparisons between approaches are much less common in the literature of LfO. The goal of this repository is to provide a set of standard domains (which hopefully will grow over time), to foster reproducibility of results and comparisons across approaches.

For a list of the domains currently in the repository, visit this wiki page: [Domains](https://github.com/santiontanon/LfODSRepository/wiki/Domains)

