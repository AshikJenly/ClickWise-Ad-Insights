import dbutils
configs = {"fs.azure.account.auth.type": "OAuth",
       "fs.azure.account.oauth.provider.type": "org.apache.hadoop.fs.azurebfs.oauth2.ClientCredsTokenProvider",
       "fs.azure.account.oauth2.client.id": "bb663583-c484-42a6-ad16-37033f63122e",
       "fs.azure.account.oauth2.client.secret": "se78Q~~ykmyP-AuEDoUSEeGUvdG2mhpLko.PBbEE",
       "fs.azure.account.oauth2.client.endpoint": "https://login.microsoftonline.com/1b9d1c72-3df1-457f-97ee-ebd194d685ba/oauth2/token",
       "fs.azure.createRemoteFileSystemDuringInitialization": "true"}

dbutils.fs.mount(
source = "abfss://clickstreamdatacontainer@datastreamanalytics.dfs.core.windows.net/<directory-name>",
mount_point = "/mnt/streamingdata",
extra_configs = configs)
