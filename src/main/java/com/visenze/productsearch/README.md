# Product Search API

---

## Table of Contents
1. [Introduction](#1-introduction)
    - 1.1 [Terminologies](#11-terminologies)
2. [APIs](#2-apis)
    - 2.1 [Search by Image](#21-search-by-image)
    - 2.2 [Search by ID](#22-search-by-id)
3. [Search Results](#3-search-results)
4. [Basic Search Parameters](#4-basic-search-parameters)
5. [Advanced Search Parameters](#5-advanced-search-parameters)
    - 5.1 [Grouping](#51-grouping)
    - 5.2 [Filtering](#52-filtering)
    - 5.3 [Visual Analytics](#53-visual-analytics)
    
---

## 1. Introduction

With the release of ViSenze's Catalog system, ViSearch Java SDK will now include
the Product Search API suite - an additional set of APIs that ideally can:

- Select the right product image for indexing for best search performance
- Aggregate search results on a product level instead of image level
- Consistent data type in API response with Catalog’s schema

### 1.1 Terminologies 

Here are some terminologies that will provide background context and help with 
better understanding of Product Search APIs.

| Term          | Summary                                                      |
| ------------- | ------------------------------------------------------------ | 
| App           | The type of product functionality that we enable             |
| Catalog       | Centralized data storage for customers to ingest their data  |
| Placement     | Where the customers want to store their App                  |

## 2. APIs

There are two main APIs provided in this suite, one allows searching for 
products based on an image input, the other searches using a product's ID. A 
product's ID can be retrieved from a [Search Result](#3-search-results).

### 2.1 Search by Image

### 2.2 Search by ID

> pseudo steps for:
> - make an initial search using an image (file, url)
> - store the search results (contains the list of products)
> - use the product ID of one of these products in the next search
> 
## 3. Search Results

## 4. Basic Search Parameters

## 5. Advanced Search Parameters

### 5.1 Grouping

### 5.2 Filtering

### 5.3 Visual Analytics