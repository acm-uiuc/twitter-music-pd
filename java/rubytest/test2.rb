require 'socket'  

require 'rubygems'
require 'json'


@payload ={
    "name" => "api-workspace",
    "title" => "API Workspace",
    "account_id" => "1"
  }.to_json

streamSock = TCPSocket.new( "127.0.0.1", 9133 )  
streamSock.write(@payload)
streamSock.close
