require 'net/http'

require 'rubygems'
require 'json'

puts 'wtf is going onnnnn?'

@host = 'localhost'
@port = '9133'

@post_ws = "/"

@payload ={
    "name" => "api-workspace",
    "title" => "API Workspace",
    "account_id" => "1"
  }.to_json
  

puts 'wtf is going onnnnn?'
def post
     req = Net::HTTP::Post.new(@post_ws, initheader = {'Content-Type' =>'application/json'})
          req.basic_auth @user, @pass
          req.body = @payload
          response = Net::HTTP.new(@host, @port).start {|http| http.request(req) }
           puts "Response #{response.code} #{response.message}:
          #{response.body}"
        end

thepost = post
puts thepost
