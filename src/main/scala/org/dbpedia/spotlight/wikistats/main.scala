/**
 *  Copyright 2015 DBpedia Spotlight
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */


package org.dbpedia.spotlight.wikistats

import java.util.Locale

import org.apache.spark.SparkContext
import org.apache.spark.sql.SQLContext
import org.dbpedia.spotlight.model.TokenType
import org.dbpedia.spotlight.db.model.Stemmer
import org.dbpedia.spotlight.db.tokenize.LanguageIndependentStringTokenizer
import scala.collection.JavaConversions._
import scala.collection.mutable.ListBuffer

/*
Entry point for Code Execution
 */
object main {

  def main(args: Array[String]): Unit ={

    //TODO - Change the input file
    val inputWikiDump = "E:\\enwiki-pages-articles-latest.xml"

    //TODO - Initialize with Proper Spark Settings
    implicit val sc = new SparkContext("local","FirstTestApp","E:\\ApacheSpark\\spark-1.4.0-bin-hadoop2.6\\bin")

    //Wikipedia Dump Language
    //TODO - To Change in future to pass the language as input arguments. Defaulting to English for testing
    val lang = "en"

    //Initializing SqlContext for Use in Operating on DataFrames
    implicit val sqlContext = new SQLContext(sc)

    /*
    Parsing and Processing Starts from here
     */
    val wikipediaParser = new JsonPediaParser(lang)

    //Read the Wikipedia XML Dump and store each page in JSON format as an element of RDD
    val pageRDDs = wikipediaParser.parse(inputWikiDump,sc)

    //Logic to create Json dataframe from the Base RDD
    val dfWikiRDD = wikipediaParser.parseJSON(pageRDDs)

    //Logic to calculate various counts
    val computeStats = new ComputeStats(lang)

    //computeStats.uriCounts(dfWikiRDD)
    //computeStats.sfCounts(wikipediaParser.getSfs())
    val allSfs = wikipediaParser.getSfs(dfWikiRDD)

    //Broadcasting variable for building FSA
    //val sfsBroadcast = sc.broadcast(allSfs)

    //Below Logic is to get Tokens from the list of Surface forms
    //val tokens = wikipediaParser.getTokens(allSfs,lang)

    //Broadcasting tokens
    //val tokenBroadcast = sc.broadcast(tokens)


  }


}
