# Task 5 AWS
1. Application Role Creation
In order to access S3 bucket from within your application deployed in EC2 you must make it under authorized identity.
The common approach is to assign a service role that we are to create beforehand.
     - Go to IAM section and navigate to Roles section.
     - Under the section Common use cases select EC2 and assign AmazonS3ReadOnlyAccess permission policy on Role.
     - Save just created Role.

2. Upload Application Jar to AWS S3
     - Create mjc-<yourname>-jar S3 bucket.
     - Make sure the bucket is available for the role created on the step above only. It might be achieved through Bucket Policy.
     - Upload your application jar file to the newly created bucket.

3. Launch RDS instance
     - Start launching RDS instance available within Free Tier (MySQL or PostgreSQL).
     - Select default VPC, Subnet and Security Group. 
     - Make sure Security Group is configured to permit access by port your database is running on.
     - Configure your database with application specific data: schema, tables, data.
4. Launch EC2 instance
     - Start launching t2.micro EC2 instance based on Amazon Linux 2 image with default VPC settings. 
     - Assign the Role created in step 2.
     - Within User Data section add the script that downloads your application from S3 bucket and launchs it. Use aws cli command: aws s3 cp s3://BUCKET-NAME/FILENAME . 
     - In security group settings open the port your application will be running on.
  ## Results were presented to mentor and loaded here as .png images 
