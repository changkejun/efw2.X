/**
 * efw framework server db library
 * @author Chang Kejun
 */
///////////////////////////////////////////////////////////////////////////////
var EfwServerDb=function(){};
///////////////////////////////////////////////////////////////////////////////
EfwServerDb.prototype={
	"open":function(jdbcResourceName){
		if(jdbcResourceName==undefined||jdbcResourceName==null)jdbcResourceName="";
		Packages.efw.db.DatabaseManager.open(jdbcResourceName);
	},
	"getSingle":function(executionParams){
		var ret=EfwServerDb.prototype.executeQuery(executionParams);
		if(ret.length>0) return ret[0];
		else return null;
	},
	"executeQuery":function(executionParams){
		var jdbcResourceName=executionParams.jdbcResourceName;
		if(jdbcResourceName==undefined||jdbcResourceName==null)jdbcResourceName="";
		var groupId=executionParams.groupId;
		var sqlId=executionParams.sqlId;
		var aryParam=executionParams.params;
		var params=new java.util.HashMap();
		for(var key in aryParam){
			var vl=aryParam[key];
			if(vl==null||vl=="")vl=null;	
			else if(vl.getTime)vl=new java.sql.Date(vl.getTime());
			params.put(key,vl);
		}
		var mapping=executionParams.mapping;
		
		var rs= Packages.efw.db.DatabaseManager.getDatabase(jdbcResourceName).executeQuery(groupId,sqlId,params);
		var ret=[];
		var meta=rs.getMetaData();
		var parseValue=function(vl){
			var value = vl;
		    if (typeof value =="object"){
				if (value==null){
					value=null;
				}else if(value.getClass().getName()=="java.lang.String"){
					value="" + value;
				}else if(value.getClass().getName()=="java.lang.Boolean"){
					value= true && value;
				}else if(value.getClass().getName()=="java.lang.Byte"
					||value.getClass().getName()=="java.lang.Short"
					||value.getClass().getName()=="java.lang.Integer"
					||value.getClass().getName()=="java.lang.Long"
					||value.getClass().getName()=="java.lang.Float"
					||value.getClass().getName()=="java.lang.Double"
					||value.getClass().getName()=="java.math.BigDecimal"){
					value=0+new Number(value);
				}else if(value.getClass().getName()=="java.sql.Date"
					||value.getClass().getName()=="java.sql.Time"
					||value.getClass().getName()=="java.sql.Timestamp"){
					var dt=new Date();dt.setTime(value.getTime());value=dt;
				}else{
					// you should do something if the comment is printed out.
					Packages.efw.log.LogManager.ErrorDebug("["+value + "] is an instance of "+value.getClass().getName()+" which has not been supported by efw.","");
				}
			}
			return value;
		};
		//change recordset to java array
		while (rs.next()) {
			var rsdata={};
			var maxColumnCount=meta.getColumnCount();
			for (var j=1;j<=maxColumnCount;j++){
				var key=meta.getColumnName(j);
				rsdata[key]=parseValue(rs.getObject(key));
			}
			ret.push(EfwServerMapping.prototype.doSingle(rsdata,mapping));
		}
		rs.close();
		Packages.efw.db.DatabaseManager.getDatabase(jdbcResourceName).closeQuery();
		return ret;
	},
	"executeUpdate":function(executionParams){
		var jdbcResourceName=executionParams.jdbcResourceName;
		if(jdbcResourceName==undefined||jdbcResourceName==null)jdbcResourceName="";
		var groupId=executionParams.groupId;
		var sqlId=executionParams.sqlId;
		var aryParam=executionParams.params;
		var params=new java.util.HashMap();
		for(var key in aryParam){
			var vl=aryParam[key];
			if(vl==null||vl=="")vl=null;	
			else if(vl.getTime)vl=new java.sql.Date(vl.getTime());
			params.put(key,vl);
		}
		return Packages.efw.db.DatabaseManager.getDatabase(jdbcResourceName).executeUpdate(groupId,sqlId,params);
	},
	"execute":function(executionParams){
		var jdbcResourceName=executionParams.jdbcResourceName;
		if(jdbcResourceName==undefined||jdbcResourceName==null)jdbcResourceName="";
		var groupId=executionParams.groupId;
		var sqlId=executionParams.sqlId;
		var aryParam=executionParams.params;
		var params=new java.util.HashMap();
		for(var key in aryParam){
			var vl=aryParam[key];
			if(vl==null||vl=="")vl=null;	
			else if(vl.getTime)vl=new java.sql.Date(vl.getTime());
			params.put(key,vl);
		}
		Packages.efw.db.DatabaseManager.getDatabase(jdbcResourceName).execute(groupId,sqlId,params);
	},
	"commit":function(jdbcResourceName){
		if(jdbcResourceName==undefined||jdbcResourceName==null)jdbcResourceName="";
		Packages.efw.db.DatabaseManager.getDatabase(jdbcResourceName).commit();
	},
	"rollback":function(jdbcResourceName){
		if(jdbcResourceName==undefined||jdbcResourceName==null)jdbcResourceName="";
		Packages.efw.db.DatabaseManager.getDatabase(jdbcResourceName).rollback();
	},
	"closeAll":function(){
		Packages.efw.db.DatabaseManager.closeAll();
	},
	"getSingleSql":function(executionParams){
		var ret=EfwServerDb.prototype.executeQuerySql(executionParams);
		if(ret.length>0) return ret[0];
		else return null;
	},
	"executeQuerySql":function(executionParams){
		var jdbcResourceName=executionParams.jdbcResourceName;
		if(jdbcResourceName==undefined||jdbcResourceName==null)jdbcResourceName="";
		var sql=executionParams.sql;
		var mapping=executionParams.mapping;
		
		var rs= Packages.efw.db.DatabaseManager.getDatabase(jdbcResourceName).executeQuerySql(sql);
		var ret=[];
		var meta=rs.getMetaData();
		var parseValue=function(vl){
			var value = vl;
		    if (typeof value =="object"){
				if (value==null){
					value=null;
				}else if(value.getClass().getName()=="java.lang.String"){
					value="" + value;
				}else if(value.getClass().getName()=="java.lang.Boolean"){
					value= true && value;
				}else if(value.getClass().getName()=="java.lang.Byte"
					||value.getClass().getName()=="java.lang.Short"
					||value.getClass().getName()=="java.lang.Integer"
					||value.getClass().getName()=="java.lang.Long"
					||value.getClass().getName()=="java.lang.Float"
					||value.getClass().getName()=="java.lang.Double"
					||value.getClass().getName()=="java.math.BigDecimal"){
					value=0+new Number(value);
				}else if(value.getClass().getName()=="java.sql.Date"
					||value.getClass().getName()=="java.sql.Time"
					||value.getClass().getName()=="java.sql.Timestamp"){
					var dt=new Date();dt.setTime(value.getTime());value=dt;
				}else{
					// you should do something if the comment is printed out.
					Packages.efw.log.LogManager.ErrorDebug("["+value + "] is an instance of "+value.getClass().getName()+" which has not been supported by efw.","");
				}
			}
			return value;
		};
		//change recordset to java array
		while (rs.next()) {
			var rsdata={};
			var maxColumnCount=meta.getColumnCount();
			for (var j=1;j<=maxColumnCount;j++){
				var key=meta.getColumnName(j);
				rsdata[key]=parseValue(rs.getObject(key));
			}
			ret.push(EfwServerMapping.prototype.doSingle(rsdata,mapping));
		}
		rs.close();
		Packages.efw.db.DatabaseManager.getDatabase(jdbcResourceName).closeQuery();
		return ret;
	},
	"executeUpdateSql":function(executionParams){
		var jdbcResourceName=executionParams.jdbcResourceName;
		if(jdbcResourceName==undefined||jdbcResourceName==null)jdbcResourceName="";
		var sql=executionParams.sql;
		return Packages.efw.db.DatabaseManager.getDatabase(jdbcResourceName).executeUpdateSql(sql);
	},
	"executeSql":function(executionParams){
		var jdbcResourceName=executionParams.jdbcResourceName;
		if(jdbcResourceName==undefined||jdbcResourceName==null)jdbcResourceName="";
		var sql=executionParams.sql;
		Packages.efw.db.DatabaseManager.getDatabase(jdbcResourceName).executeSql(sql);
	},
};
///////////////////////////////////////////////////////////////////////////////
EfwServer.prototype.db=new EfwServerDb();

