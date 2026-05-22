## komendy dla aws
aws configure
aws sts get-caller-identity
aws --version

## komendy dla terraform
terraform --version

cd aws
terraform init
terraform plan
terraform apply

terraform state list

ssh -i ~/.ssh/kaz_to_git ubuntu@18.199.82.207
tail -f /var/log/cloud-init-output.log

terraform destroy
