val Hospicecareambulatory = spark.sqlContext.sql(s"Select QD.patientId, QD.qdmElementId from  QdmElements AS QD where dataType = 'INTERVENTION_PERFORMED' and array_contains(VALUE_SET_OID ,'2.16.840.1.113762.1.4.1108.15')")
Hospicecareambulatory.createOrReplaceTempView("Hospicecareambulatory")

val HospiceOrder = spark.sqlContext.sql(s"Select patientId, qdmElementId  from Hospicecareambulatory")
HospiceOrder.createOrReplaceTempView("HospiceOrder")

val HospicePerformed = spark.sqlContext.sql(s"Select patientId, qdmElementId  from Hospicecareambulatory")
HospicePerformed.createOrReplaceTempView("HospicePerformed")

val HospiceExclusions = spark.sqlContext.sql(s"Select QD.patientId, QD.qdmElementId from  QdmElements AS QD")
HospiceExclusions.createOrReplaceTempView("HospiceExclusions")

val StatusPostRightMastectomy = spark.sqlContext.sql(s"Select QD.patientId, QD.qdmElementId from  QdmElements AS QD where dataType = 'DIAGNOSIS' and array_contains(VALUE_SET_OID ,'2.16.840.1.113883.3.464.1003.198.12.1070')")
StatusPostRightMastectomy.createOrReplaceTempView("StatusPostRightMastectomy")

val UnilateralMastectomy_UnspecifiedLaterality = spark.sqlContext.sql(s"Select QD.patientId, QD.qdmElementId from  QdmElements AS QD where dataType = 'DIAGNOSIS' and array_contains(VALUE_SET_OID ,'2.16.840.1.113883.3.464.1003.198.12.1071')")
UnilateralMastectomy_UnspecifiedLaterality.createOrReplaceTempView("UnilateralMastectomy_UnspecifiedLaterality")

val UnilatMastectomy = spark.sqlContext.sql(s"Select QD.patientId, QD.qdmElementId from  QdmElements AS QD")
UnilatMastectomy.createOrReplaceTempView("UnilatMastectomy")

val RightMastectomy = spark.sqlContext.sql(s"Select patientId, qdmElementId from ((Select patientId, qdmElementId from StatusPostRightMastectomy) UNION (Select patientId, qdmElementId from UnilatMastectomy)) AS RightMastectomy where Before(RightMastectomy.startDate, MP.stopDate)")
RightMastectomy.createOrReplaceTempView("RightMastectomy")

val RightMast = spark.sqlContext.sql(s"Select patientId, qdmElementId  from RightMastectomy")
RightMast.createOrReplaceTempView("RightMast")

val StatusPostLeftMastectomy = spark.sqlContext.sql(s"Select QD.patientId, QD.qdmElementId from  QdmElements AS QD where dataType = 'DIAGNOSIS' and array_contains(VALUE_SET_OID ,'2.16.840.1.113883.3.464.1003.198.12.1069')")
StatusPostLeftMastectomy.createOrReplaceTempView("StatusPostLeftMastectomy")

val LeftMastectomy = spark.sqlContext.sql(s"Select patientId, qdmElementId from ((Select patientId, qdmElementId from StatusPostLeftMastectomy) UNION (Select patientId, qdmElementId from UnilatMastectomy)) AS LeftMastectomy where Before(LeftMastectomy.startDate, MP.stopDate)")
LeftMastectomy.createOrReplaceTempView("LeftMastectomy")

val LeftMast = spark.sqlContext.sql(s"Select patientId, qdmElementId  from LeftMastectomy")
LeftMast.createOrReplaceTempView("LeftMast")

val Historyofbilateralmastectomy = spark.sqlContext.sql(s"Select QD.patientId, QD.qdmElementId from  QdmElements AS QD where dataType = 'DIAGNOSIS' and array_contains(VALUE_SET_OID ,'2.16.840.1.113883.3.464.1003.198.12.1068')")
Historyofbilateralmastectomy.createOrReplaceTempView("Historyofbilateralmastectomy")

val History = spark.sqlContext.sql(s"Select patientId, qdmElementId  from History")
History.createOrReplaceTempView("History")

val BilateralMastectomy = spark.sqlContext.sql(s"Select QD.patientId, QD.qdmElementId from  QdmElements AS QD where dataType = 'PROCEDURE_PERFORMED' and array_contains(VALUE_SET_OID ,'2.16.840.1.113883.3.464.1003.198.12.1005')")
BilateralMastectomy.createOrReplaceTempView("BilateralMastectomy")

val BM = spark.sqlContext.sql(s"Select patientId, qdmElementId from Select QD.patientId, QD.qdmElementId from  QdmElements AS QD Inner Join BilateralMastectomy ON  QD.patientId = BilateralMastectomy.patientId AS BM where Before(BM.stopDate, MP.stopDate)")
BM.createOrReplaceTempView("BM")

val BMPerformed = spark.sqlContext.sql(s"Select patientId, qdmElementId  from BM")
BMPerformed.createOrReplaceTempView("BMPerformed")

val DenominatorExclusions = spark.sqlContext.sql(s"Select patientId, qdmElementId from Select QD.patientId, QD.qdmElementId from  QdmElements AS QD AS DenominatorExclusions where Count(UMPerformed) = 2")
DenominatorExclusions.createOrReplaceTempView("DenominatorExclusions")

val rs125 = sqlContext.sql(s"select $providerId AS providerId ,a.patientId, case when b.patientId is NULL then 0 else 1 end as ipp from QdmElements a left join InitialPopulation b on a.patientid = b.patientid" ).distinct();rs125.createOrReplaceTempView("rs125");

val rs125 = sqlContext.sql(s"select a.*, case when b.patientId is NULL then 0 else 1 end as denominator from rs125 a left join Denominator b  on a.patientid = b.patientid").distinct();
rs125.createOrReplaceTempView("rs125");